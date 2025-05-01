package com.nomEmpresa.nomProyecto.dto.wasabi.modelos;

import org.springframework.web.multipart.MultipartFile;

public record GaleriaMultipartDTO (
        String idGaleria,
        String nombre,
        MultipartFile imagenPerfil,
        MultipartFile imagenBanner
){
}
