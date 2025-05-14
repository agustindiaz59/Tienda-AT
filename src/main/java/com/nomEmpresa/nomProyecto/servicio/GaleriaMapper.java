package com.nomEmpresa.nomProyecto.servicio;

import com.nomEmpresa.nomProyecto.dto.wasabi.modelos.GaleriaDTO;
import com.nomEmpresa.nomProyecto.dto.wasabi.modelos.MultimediaDTO;
import com.nomEmpresa.nomProyecto.modelos.Galeria;
import com.nomEmpresa.nomProyecto.modelos.Multimedia;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public abstract class GaleriaMapper {

    public static GaleriaDTO galeriaDTO(
            Galeria galeria,
            Boolean archivos
    ) {

        Set<MultimediaDTO> dtoArchivos;

        if (!archivos) {
            dtoArchivos = Set.of(); //Un set vacio para evitar Lazy Loading de las galerias
        } else {
            dtoArchivos = galeria.getMultimedias()
                    .stream()
                    .map(Multimedia::getDTO)
                    .collect(Collectors.toSet());
        }

        if (galeria.getImgPerfil() == null && galeria.getImgBanner() == null)
            return new GaleriaDTO(
                    galeria.getIdGaleria(),
                    dtoArchivos,
                    galeria.getNombre(),
                    galeria.getFechaDeCreacion(),
                    galeria.getNotas()
            );
        if (galeria.getImgBanner() == null)
            return new GaleriaDTO(
                    galeria.getIdGaleria(),
                    galeria.getImgPerfil().getDTO(),
                    dtoArchivos,
                    galeria.getNombre(),
                    galeria.getFechaDeCreacion(),
                    galeria.getNotas()
            );
        if (galeria.getImgPerfil() == null)
            return new GaleriaDTO(
                    galeria.getIdGaleria(),
                    dtoArchivos,
                    galeria.getNombre(),
                    galeria.getFechaDeCreacion(),
                    galeria.getImgBanner().getDTO(),
                    galeria.getNotas()
            );
        return new GaleriaDTO(
                galeria.getIdGaleria(),
                galeria.getImgPerfil().getDTO(),
                galeria.getImgBanner().getDTO(),
                dtoArchivos,
                galeria.getNombre(),
                galeria.getFechaDeCreacion(),
                galeria.getNotas()
        );
    }


}