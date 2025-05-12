package com.nomEmpresa.nomProyecto.dto;

import jakarta.validation.constraints.NotEmpty;

public record AdministradorDTO (
        @NotEmpty
        String nombre,
        @NotEmpty
        String contrasenia
){
}
