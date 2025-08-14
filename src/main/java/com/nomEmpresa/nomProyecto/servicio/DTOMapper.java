package com.nomEmpresa.nomProyecto.servicio;

import com.nomEmpresa.nomProyecto.dto.modelos.*;
import com.nomEmpresa.nomProyecto.modelos.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public abstract class DTOMapper {

    /**
     *
     * Toma una galeria y la mapea a un DTO equivalente
     *
     * @param galeria galeria en cuestión
     * @param archivos incluir archivos
     * @param notas incluir notas
     * @return DTO listo para exponer
     */
    public static GaleriaDTO galeriaDTO(
            Galeria galeria,
            Boolean archivos,
            Boolean notas
    ) {

        //Auxiliares
        Set<MultimediaDTO> dtoArchivos;
        List<NotaDTO> dtoNotas;

        //Verifico si se quieren o no los archivos y las notas
        if (!archivos) {
            dtoArchivos = Set.of(); //Un set vacio para evitar Lazy Loading de las galerias
        } else {
            dtoArchivos = galeria.getMultimedias()
                    .stream()
                    .map(Multimedia::getDTO)
                    .collect(Collectors.toSet());
        }

        if(!notas){
            dtoNotas = List.of();
        } else{
            dtoNotas = notasDTO(galeria.getNotas());
        }

        //Instancio un DTO segun los datos disponibles en la galeria
        if (galeria.getImgPerfil() == null && galeria.getImgBanner() == null)
            return new GaleriaDTO(
                    galeria.getIdGaleria(),
                    dtoArchivos,
                    galeria.getNombre(),
                    galeria.getFechaDeCreacion(),
                    dtoNotas
            );
        if (galeria.getImgBanner() == null)
            return new GaleriaDTO(
                    galeria.getIdGaleria(),
                    galeria.getImgPerfil().getDTO(),
                    dtoArchivos,
                    galeria.getNombre(),
                    galeria.getFechaDeCreacion(),
                    dtoNotas
            );
        if (galeria.getImgPerfil() == null)
            return new GaleriaDTO(
                    galeria.getIdGaleria(),
                    dtoArchivos,
                    galeria.getNombre(),
                    galeria.getFechaDeCreacion(),
                    galeria.getImgBanner().getDTO(),
                    dtoNotas
            );
        return new GaleriaDTO(
                galeria.getIdGaleria(),
                galeria.getImgPerfil().getDTO(),
                galeria.getImgBanner().getDTO(),
                dtoArchivos,
                galeria.getNombre(),
                galeria.getFechaDeCreacion(),
                dtoNotas
        );
    }


    /**
     *
     * Toma una lista de notas y las mapea a una lista de DTO's listos para exponer
     *
     * @param notas Notas en cuestion
     * @return DTO's listos para exponer
     */
    public static List<NotaDTO> notasDTO(List<Nota> notas){
        return notas.stream()
                .map(n -> new NotaDTO(n.getContenido(), n.getHora()))
                .toList();
    }

    public static DatosAuxiliaresDTO getDatosAuxiliaresDTO(DatosAuxiliares crudo){
        return new DatosAuxiliaresDTO(
                //Datos principales
                crudo.getPresentacion(),
                crudo.getDireccion(),
                crudo.getCelular(),
                crudo.getHorario(),
                crudo.getEmail(),
                crudo.getFacebook(),
                crudo.getInstagram(),
                crudo.getWhatsapp(),
                //Relaciones
                getServiciosDto(crudo.getServicios())
        );
    }




    /**
     *
     * Toma un conjunto de servicios y los mapea a un conjunto de DTO's del mismo tipo
     *
     * @param servicios servicios en crudo
     * @return DTO's listos para exponer
     */
    public static Set<ServicioDto> getServiciosDto(Set<Servicio> servicios){
        return servicios.stream()
                .map( servicio ->
                        new ServicioDto(
                                servicio.getTitulo(),
                                servicio.getSubtitulo(),
                                servicio.getDescripcion(),
                                servicio.getIncluido(),
                                servicio.getExclusivo(),
                                servicio.getNotas(),
                                servicio.getTipo(),
                                servicio.getPrecio()
                                )
                )
                .collect(Collectors.toSet());
    }


}