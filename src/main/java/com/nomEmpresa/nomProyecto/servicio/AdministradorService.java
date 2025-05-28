package com.nomEmpresa.nomProyecto.servicio;

import com.nomEmpresa.nomProyecto.dto.AdministradorDTO;
import com.nomEmpresa.nomProyecto.modelos.Administrador;
import com.nomEmpresa.nomProyecto.repositorio.IAdministradorRepository;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.Optional;

@Service
public class AdministradorService {


    private IAdministradorRepository administradorRepository;

    private PasswordEncoder passwordEncoder;





    @Autowired
    public AdministradorService(IAdministradorRepository administradorRepository, PasswordEncoder passwordEncoder) {
        this.administradorRepository = administradorRepository;
        this.passwordEncoder = passwordEncoder;
    }









    @Transactional
    public ResponseEntity<AdministradorDTO> cambiarContraseniaAdmin(
        AdministradorDTO dto
    ){
        //Verifico si existe en el sistema
        Optional<Administrador> administrador = administradorRepository.findByNombre(dto.nombre());

        if(administrador.isEmpty()){
            return ResponseEntity
                    .status(HttpStatusCode.NOT_ACCEPTABLE)
                    .body(dto);
        }


        // Creo o sobreescribo el administrador
        return crearAdmin(dto);
    }

    public ResponseEntity<AdministradorDTO> crearAdmin(AdministradorDTO dto) {

        // Sobreescribo o creo el administrador con el mismo nombre
        administradorRepository.save(new Administrador(dto.nombre(), passwordEncoder.encode(dto.contrasenia())));

        return ResponseEntity
                .ok(dto);
    }

    public ResponseEntity<AdministradorDTO> eliminarAdministrador(String nombreUsuario) {

        if(!administradorRepository.existsByNombre(nombreUsuario)){

            return ResponseEntity.notFound().build();

        } else{

            administradorRepository.deleteByNombre(nombreUsuario);
            return ResponseEntity
                    .status(HttpStatusCode.ACCEPTED)
                    .build();

        }

    }
}
