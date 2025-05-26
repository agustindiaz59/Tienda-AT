package com.nomEmpresa.nomProyecto.dto.modelos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class GaleriaPage{

    private List<GaleriaDTO> galerias;
    private Integer paginaActual;
    private Integer totalDePaginas;
    private Integer tamaño;
    private Long totalDeElementos;


}
