package com.nomEmpresa.nomProyecto.servicio;

import com.github.benmanes.caffeine.cache.Cache;
import com.nomEmpresa.nomProyecto.dto.AdministradorDTO;
import com.nomEmpresa.nomProyecto.modelos.Administrador;
import com.nomEmpresa.nomProyecto.repositorio.IAdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AdministradorService {

    //Dependencias
    private final IAdministradorRepository administradorRepository;

    private final PasswordEncoder passwordEncoder;







    @Autowired
    public AdministradorService(IAdministradorRepository administradorRepository, PasswordEncoder passwordEncoder) {
        this.administradorRepository = administradorRepository;
        this.passwordEncoder = passwordEncoder;
    }











    @Transactional
    @CachePut("ADMINISTRADOR")
    public ResponseEntity<AdministradorDTO> cambiarContraseniaAdmin(
        AdministradorDTO dto
    ){
        //Verifico si existe en el sistema
        Optional<Administrador> administrador = administradorRepository.findByNombre(dto.nombre());

        if(administrador.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(dto);
        }


        // Creo o sobreescribo el administrador
        return crearAdmin(dto);
    }

    @CacheEvict("ADMINISTRADOR")
    public ResponseEntity<AdministradorDTO> crearAdmin(AdministradorDTO dto) {

        // Sobreescribo o creo el administrador con el mismo nombre
        administradorRepository.save(new Administrador(dto.nombre(), passwordEncoder.encode(dto.contrasenia())));

        return ResponseEntity
                .ok(dto);
    }


    @CacheEvict("ADMINISTRADOR")
    public ResponseEntity<AdministradorDTO> eliminarAdministrador(String nombreUsuario) {

        if(!administradorRepository.existsByNombre(nombreUsuario)){

            return ResponseEntity.notFound().build();

        } else{

            administradorRepository.deleteByNombre(nombreUsuario);
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .build();

        }

    }



    @Cacheable(value = "ADMINISTRADOR")
    public Administrador consultarAdministrador(String nombre){
        return administradorRepository.findByNombre(nombre).orElse(null);
    }



    public ResponseEntity<AdministradorDTO> verificarUsuario(AdministradorDTO dto) {
        Optional<Administrador> administrador = Optional.of(consultarAdministrador(dto.nombre()));

        //No existe en la base de datos
        if(administrador.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(dto);
        }


        boolean existe = passwordEncoder.matches(dto.contrasenia(),administrador.get().getPassword());

        if (existe){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(dto);
        }else{
            //No coincide la contraseña
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(dto);
        }
    }
}
