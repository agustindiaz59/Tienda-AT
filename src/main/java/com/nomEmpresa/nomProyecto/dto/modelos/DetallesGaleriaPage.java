package com.nomEmpresa.nomProyecto.dto.modelos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nomEmpresa.nomProyecto.modelos.Galeria;
import com.nomEmpresa.nomProyecto.modelos.Multimedia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Component
public class DetallesGaleriaPage {

    private String idGaleria;

    @JsonProperty("imagen-perfil")
    private MultimediaDTO imagenPerfil;

    @JsonProperty("imagen-banner")
    private MultimediaDTO imagenBanner;

    //Debe empezar vacio
    //Debe ser un List para no eliminar los repetidos
    private List<MultimediaDTO> archivos;
    //

    private String nombre;

    private Instant fechaDeCreacion;

    private List<String> notas;

    //Campos de paginacion
    private Integer paginaActual;
    private Integer totalDePaginas;
    private Integer tamaño;
    private Integer totalDeElementos;


    public DetallesGaleriaPage(
            Galeria galeria,
            Page<Multimedia> multimediaPage
    ){

        GaleriaDTO galeriaDTO = galeria.getDTO();

        //Campos de galeria
        this.setIdGaleria(galeriaDTO.idGaleria());
        this.setNombre(galeriaDTO.nombre());
        this.setImagenPerfil(galeriaDTO.imagenPerfil());
        this.setImagenBanner(galeriaDTO.imagenBanner());
        this.setFechaDeCreacion(galeriaDTO.fechaDeCreacion());
        this.setNotas(galeriaDTO.notas());

        //Pageable usa una List<> asi que casteo a una Set<>
        List<MultimediaDTO> multimediaDTOS = multimediaPage.getContent()
                .stream()
                .map(Multimedia::getDTO)
                .collect(Collectors.toList());

        this.setArchivos(multimediaDTOS);

        //Campos de paginacion
        this.setPaginaActual(multimediaPage.getNumber());
        this.setTotalDePaginas(multimediaPage.getTotalPages());
        this.setTamaño(multimediaPage.getSize());
        this.setTotalDeElementos(Integer.parseInt(String.valueOf(multimediaPage.getTotalElements())));
    }


}
