package com.nomEmpresa.nomProyecto.dto.modelos;

import org.springframework.web.multipart.MultipartFile;

@Deprecated
public record GaleriaMultipartDTO (
        String idGaleria,
        String nombre,
        MultipartFile imagenPerfil,
        MultipartFile imagenBanner
){
}
