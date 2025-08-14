package com.nomEmpresa.nomProyecto.dto.modelos;

import java.util.Set;

public record DatosAuxiliaresDTO(
        //Datos principales
        String presentacion,
        String direccion,
        String celular,
        String horario,
        String email,
        String facebook,
        String instagram,
        String whatsapp,

        //Relaciones
        Set<ServicioDto> servicios
) {
}
