package com.nomEmpresa.nomProyecto.repositorio;

import com.nomEmpresa.nomProyecto.modelos.Galeria;
import com.nomEmpresa.nomProyecto.modelos.Multimedia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;


@Repository
public interface IMultimediaRepository extends JpaRepository<Multimedia, Long> {

    Page<Multimedia> findByIdGaleriaAndFechaModificadoAfter(
            Galeria idGaleria,
            Instant fechaModificado,
            Pageable pageable
    );

    void deleteBySrc(String urlMultimedia);

}
