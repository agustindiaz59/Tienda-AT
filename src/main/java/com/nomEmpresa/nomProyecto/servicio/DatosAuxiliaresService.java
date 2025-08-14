package com.nomEmpresa.nomProyecto.servicio;

import com.nomEmpresa.nomProyecto.dto.modelos.DatosAuxiliaresDTO;
import com.nomEmpresa.nomProyecto.dto.modelos.ServicioDto;
import com.nomEmpresa.nomProyecto.modelos.DatosAuxiliares;
import com.nomEmpresa.nomProyecto.modelos.Servicio;
import com.nomEmpresa.nomProyecto.repositorio.IDatosAuxiliares;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class DatosAuxiliaresService {

    private final IDatosAuxiliares repositorio;

    public DatosAuxiliaresService(IDatosAuxiliares repositorio){
        this.repositorio = repositorio;
    }

    public ResponseEntity<DatosAuxiliaresDTO> editar(DatosAuxiliaresDTO dto){
        //Buscar el registro en la BBDD, id por defecto 1
        DatosAuxiliares nuevo = new DatosAuxiliares();

        //Actualizar el registro
        nuevo.setCelular(dto.celular());
        nuevo.setDireccion(dto.direccion());
        nuevo.setPresentacion(dto.presentacion());
        nuevo.setHorario(dto.horario());
        nuevo.setEmail(dto.email());
        nuevo.setFacebook(dto.facebook());
        nuevo.setInstagram(dto.instagram());
        nuevo.setWhatsapp(dto.whatsapp());

        //Guardar cambios
        repositorio.save(nuevo);

        //Devolver respuesta
        if(repositorio.findById(1L).orElse(null) != null){ //Verifico si existe un registro con id = 1
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .lastModified(Instant.now())
                    .body(dto);
        }else{
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(dto);
        }
    }

    public ResponseEntity<DatosAuxiliaresDTO> traer(){
        //Traigo los datos de la BBDD
        Optional<DatosAuxiliares> datos = repositorio.findById(1L);

        //Si el registro existe lo devuelve cómo DTO, si no, devuelve un 500
        return datos.map(datosAuxiliares -> ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(DTOMapper.getDatosAuxiliaresDTO(datosAuxiliares))).orElseGet(() -> ResponseEntity
                .internalServerError()
                .build());
    }

    public ResponseEntity<DatosAuxiliaresDTO> agregarServicio(ServicioDto servicioDto) {
        //Registro en BBDD
        Optional<DatosAuxiliares> datos = repositorio.findById(1L);


        //Nuevo servicio
        Servicio nuevo = new Servicio();
        nuevo.setTitulo(servicioDto.titulo());
        nuevo.setSubtitulo(servicioDto.subtitulo());
        nuevo.setDescripcion(servicioDto.descripcion());
        nuevo.setPrecio(servicioDto.precio());
        nuevo.setTipo(servicioDto.tipo());
        nuevo.setIncluido(servicioDto.incluido());
        nuevo.setExclusivo(servicioDto.exclusivo());
        nuevo.setNotas(servicioDto.notas());

        // Guardar cambios
        datos.get().agregarServicio(nuevo);
        repositorio.save(datos.get());

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(DTOMapper.getDatosAuxiliaresDTO(datos.get()));
    }
}
