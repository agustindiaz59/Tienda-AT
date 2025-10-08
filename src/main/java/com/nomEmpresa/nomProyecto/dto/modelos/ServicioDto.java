package com.nomEmpresa.nomProyecto.dto.modelos;


import java.util.List;

public record ServicioDto(
        String titulo,
        String tituloExtra,
        String subtitulo,
        String descripcion,
        List<String>incluido,
        List<String> exclusivo,
        String notas,
        String tipo,
        Float precio
) {
}
