package com.nomEmpresa.nomProyecto.dto.modelos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public record GaleriaDTO(
        String idGaleria,
        @JsonProperty("imagen-perfil")
        MultimediaDTO imagenPerfil,
        @JsonProperty("imagen-banner")
        MultimediaDTO imagenBanner,
        Set<MultimediaDTO> archivos,
        String nombre,
        Instant fechaDeCreacion,
        List<NotaDTO> notas
) {



    //Constructor auxiliar para galerias sin foto de perfil ni banner
    public GaleriaDTO(
            String idGaleria,
            Set<MultimediaDTO> archivos,
            String nombre,
            Instant fechaDeCreacion,
            List<NotaDTO> notas
    ){
        this(idGaleria,null,null,archivos,nombre,fechaDeCreacion, notas);
    }



    //Constructor auxiliar para galerias sin foto de banner
    public GaleriaDTO(
            String idGaleria,
            MultimediaDTO imagenPerfil,
            Set<MultimediaDTO> archivos,
            String nombre,
            Instant fechaDeCreacion,
            List<NotaDTO> notas
    ){
        this(idGaleria,imagenPerfil,null,archivos,nombre,fechaDeCreacion,notas);
    }




    //Constructor auxiliar para galerias sin foto de perfil
    public GaleriaDTO(
            String idGaleria,
            Set<MultimediaDTO> archivos,
            String nombre,
            Instant fechaDeCreacion,
            MultimediaDTO imagenBanner,
            List<NotaDTO> notas
    ){
        this(idGaleria,null,imagenBanner,archivos,nombre,fechaDeCreacion, notas);
    }


    //Constructor auxiliar para galerias sin notas
    public GaleriaDTO(
            String idGaleria,
            MultimediaDTO imagenPerfil,
            Set<MultimediaDTO> archivos,
            String nombre,
            Instant fechaDeCreacion,
            MultimediaDTO imagenBanner,
            List<NotaDTO> notas
    ){
        this(idGaleria,imagenPerfil, imagenBanner,archivos,nombre,fechaDeCreacion, notas);
    }


}
