package com.nomEmpresa.nomProyecto.dto.respuestas;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nomEmpresa.nomProyecto.dto.modelos.GaleriaDTO;
import com.nomEmpresa.nomProyecto.dto.modelos.MultimediaDTO;
import com.nomEmpresa.nomProyecto.dto.modelos.NotaDTO;
import com.nomEmpresa.nomProyecto.modelos.Galeria;
import com.nomEmpresa.nomProyecto.modelos.Multimedia;
import com.nomEmpresa.nomProyecto.servicio.DTOMapper;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Component
@Builder
public class DetallesGaleriaPage {

    private String idGaleria;

    @JsonProperty("imagen-perfil")
    private MultimediaDTO imagenPerfil;

    @JsonProperty("imagen-banner")
    private MultimediaDTO imagenBanner;

    //Debe empezar vacio
    //Debe ser un List para no eliminar los repetidos
    private List<MultimediaDTO> archivos;
    private String nombre;
    private Instant fechaDeCreacion;
    private List<NotaDTO> notas;

    //Campos de paginacion
    private Integer paginaActual;
    private Integer totalDePaginas;
    private Integer tamaño;
    private Integer totalDeElementos;


    public DetallesGaleriaPage(
            Galeria galeria,
            Page<Multimedia> multimediaPage
    ){

        GaleriaDTO galeriaDTO = DTOMapper.galeriaDTO(galeria,true, true);


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
