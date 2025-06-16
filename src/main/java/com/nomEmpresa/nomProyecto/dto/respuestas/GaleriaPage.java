package com.nomEmpresa.nomProyecto.dto.respuestas;

import com.nomEmpresa.nomProyecto.dto.modelos.GaleriaDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GaleriaPage{

    private List<GaleriaDTO> galerias;
    private Integer paginaActual;
    private Integer totalDePaginas;
    private Integer tamaño;
    private Long totalDeElementos;


}
