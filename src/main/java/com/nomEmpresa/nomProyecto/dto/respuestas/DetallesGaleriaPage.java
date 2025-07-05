package com.nomEmpresa.nomProyecto.dto.respuestas;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nomEmpresa.nomProyecto.dto.modelos.GaleriaDTO;
import com.nomEmpresa.nomProyecto.dto.modelos.MultimediaDTO;
import com.nomEmpresa.nomProyecto.dto.modelos.NotaDTO;
import com.nomEmpresa.nomProyecto.modelos.Galeria;
import com.nomEmpresa.nomProyecto.modelos.Multimedia;
import com.nomEmpresa.nomProyecto.modelos.Nota;
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

    private String nombre;

    private Instant fechaDeCreacion;

    //Multimedias
    private PaginaPersonalizada<MultimediaDTO> fotosPage;

    //Notas
    private PaginaPersonalizada<NotaDTO> notasPage;







    public DetallesGaleriaPage(
            Galeria galeria,
            Page<Multimedia> multimediaPage,
            Page<Nota> notasPage
    ){

        GaleriaDTO galeriaDTO = DTOMapper.galeriaDTO(galeria,true, true);


        //Campos de galeria
        this.setIdGaleria(galeriaDTO.idGaleria());
        this.setNombre(galeriaDTO.nombre());
        this.setImagenPerfil(galeriaDTO.imagenPerfil());
        this.setImagenBanner(galeriaDTO.imagenBanner());
        this.setFechaDeCreacion(galeriaDTO.fechaDeCreacion());


        //Iniciar las fotos
        PaginaPersonalizada<MultimediaDTO> fotos = PaginaPersonalizada
                .<MultimediaDTO>builder()
                .contenido(multimediaPage.getContent()
                        .stream()
                        .map(Multimedia::getDTO)
                        .toList()
                )
                .paginaActual(multimediaPage.getNumber())
                .totalDePaginas(multimediaPage.getTotalPages())
                .tamaño(multimediaPage.getSize())
                .totalDeElementos(multimediaPage.getTotalElements())
                .build();
        this.setFotosPage(fotos);



        //Iniciar las notas
        PaginaPersonalizada<NotaDTO> notasResp = PaginaPersonalizada.
                <NotaDTO>builder()
                .contenido(DTOMapper.notasDTO(notasPage.getContent()))
                .paginaActual(notasPage.getNumber())
                .totalDePaginas(notasPage.getTotalPages())
                .tamaño(notasPage.getSize())
                .totalDeElementos(notasPage.getTotalElements())
                .build();
        this.setNotasPage(notasResp);

    }

}
