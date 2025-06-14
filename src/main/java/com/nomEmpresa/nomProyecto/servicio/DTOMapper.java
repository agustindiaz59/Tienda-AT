package com.nomEmpresa.nomProyecto.servicio;

import com.nomEmpresa.nomProyecto.dto.modelos.GaleriaDTO;
import com.nomEmpresa.nomProyecto.dto.modelos.MultimediaDTO;
import com.nomEmpresa.nomProyecto.dto.modelos.NotaDTO;
import com.nomEmpresa.nomProyecto.modelos.Galeria;
import com.nomEmpresa.nomProyecto.modelos.Multimedia;
import com.nomEmpresa.nomProyecto.modelos.Nota;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public abstract class DTOMapper {

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


    public static List<NotaDTO> notasDTO(List<Nota> notas){
        return notas.stream()
                .map(n -> new NotaDTO(n.getContenido(), n.getHora()))
                .toList();
    }


}