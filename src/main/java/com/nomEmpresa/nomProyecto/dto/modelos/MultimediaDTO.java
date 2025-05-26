package com.nomEmpresa.nomProyecto.dto.modelos;

import java.time.Instant;

public record MultimediaDTO(
        String src,
        Instant lastUpdated
) {
}
