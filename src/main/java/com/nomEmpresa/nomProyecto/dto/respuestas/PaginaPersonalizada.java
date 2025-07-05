package com.nomEmpresa.nomProyecto.dto.respuestas;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaginaPersonalizada<T> {

    protected List<T> contenido;
    protected Integer paginaActual;
    protected Integer totalDePaginas;
    protected Integer tamaño;
    protected Long totalDeElementos;
}
