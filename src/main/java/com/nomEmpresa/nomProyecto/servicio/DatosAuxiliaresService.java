package com.nomEmpresa.nomProyecto.servicio;

import com.nomEmpresa.nomProyecto.dto.modelos.DatosAuxiliaresDTO;
import com.nomEmpresa.nomProyecto.dto.modelos.ServicioDto;
import com.nomEmpresa.nomProyecto.modelos.DatosAuxiliares;
import com.nomEmpresa.nomProyecto.modelos.Servicio;
import com.nomEmpresa.nomProyecto.repositorio.IDatosAuxiliares;
import com.nomEmpresa.nomProyecto.repositorio.IServicioRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class DatosAuxiliaresService {

    private final IDatosAuxiliares repositorio;

    private final IServicioRepository servicioRepository;




    public DatosAuxiliaresService(IDatosAuxiliares repositorio, IServicioRepository servicioRepository){
        this.repositorio = repositorio;
        this.servicioRepository = servicioRepository;
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
            return aceptado(nuevo);
        }else{
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("ERROR","cannot_create")
                    .body(dto);
        }
    }








    public ResponseEntity<DatosAuxiliaresDTO> traer(){
        //Traigo los datos de la BBDD
        Optional<DatosAuxiliares> datos = repositorio.findById(1L);

        //Si el registro existe lo devuelve cómo DTO, si no, devuelve un 501
        if(datos.isEmpty()){
            return sinRegistroExistente();
        }else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(DTOMapper.getDatosAuxiliaresDTO(datos.get()));
        }
    }









    public ResponseEntity<DatosAuxiliaresDTO> agregarServicio(ServicioDto servicioDto) {
        //Registro en BBDD
        Optional<DatosAuxiliares> datos = repositorio.findById(1L);

        if(datos.isEmpty()){
            return sinRegistroExistente();
        }

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
        nuevo.setDatosAuxiliares(datos.get());

        // Guardar cambios
        datos.get().agregarServicio(nuevo);
        repositorio.save(datos.get());

        return aceptado(repositorio.findById(1L).get());
    }


    @Transactional
    public ResponseEntity<DatosAuxiliaresDTO> eliminarServicio(String titulo) {
        Optional<DatosAuxiliares> datos = repositorio.findById(1L);

        //Verifico que exista almenos un registro
        if(datos.isEmpty()){
            return sinRegistroExistente();
        }

        //Eliminacion fisica del servicio
        servicioRepository.eliminar(titulo);

        return aceptado(datos.get());
    }


    /**
     * Respuesta en caso de que no haya un primer registro en la BBDD
     *
     * @return Http 501
     */
    private ResponseEntity<DatosAuxiliaresDTO> sinRegistroExistente(){
            return ResponseEntity
                    .status(HttpStatus.NOT_IMPLEMENTED)
                    .header("Warning","Registrar almenos un registro en la BBDD")
                    .body(null);
    }


    /**
     * Respuesta Exitosa
     *
     * @param datos Cuerpo de la solicitud
     * @return Http 202
     */
    private ResponseEntity<DatosAuxiliaresDTO> aceptado(DatosAuxiliares datos){
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .lastModified(Instant.now())
                .body(DTOMapper.getDatosAuxiliaresDTO(datos));
    }
}
