package com.nomEmpresa.nomProyecto.dto.wasabi.modelos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Set;

public record GaleriaDTO(
        String idGaleria,
        @JsonProperty("imagen-perfil")
        MultimediaDTO imagenPerfil,
        @JsonProperty("imagen-banner")
        MultimediaDTO imagenBanner,
        Set<MultimediaDTO> archivos,
        String nombre,
        LocalDate fechaDeCreacion
) {



    //Constructor auxiliar para galerias sin foto de perfil ni banner
    public GaleriaDTO(
            String idGaleria,
            Set<MultimediaDTO> archivos,
            String nombre,
            LocalDate fechaDeCreacion
    ){
        this(idGaleria,null,null,archivos,nombre,fechaDeCreacion);
    }



    //Constructor auxiliar para galerias sin foto de banner
    public GaleriaDTO(
            String idGaleria,
            MultimediaDTO imagenPerfil,
            Set<MultimediaDTO> archivos,
            String nombre,
            LocalDate fechaDeCreacion
    ){
        this(idGaleria,imagenPerfil,null,archivos,nombre,fechaDeCreacion);
    }




    //Constructor auxiliar para galerias sin foto de perfil
    public GaleriaDTO(
            String idGaleria,
            Set<MultimediaDTO> archivos,
            String nombre,
            LocalDate fechaDeCreacion,
            MultimediaDTO imagenBanner
    ){
        this(idGaleria,null,imagenBanner,archivos,nombre,fechaDeCreacion);
    }


}
