package com.nomEmpresa.nomProyecto.dto.respuestas;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class PaginaPersonalizada<T> {

    protected List<T> contenido;
    protected Integer paginaActual;
    protected Integer totalDePaginas;
    protected Integer tamaño;
    protected Integer totalDeElementos;
}
