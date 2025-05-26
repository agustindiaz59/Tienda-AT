package com.nomEmpresa.nomProyecto.repositorio;

import com.nomEmpresa.nomProyecto.modelos.Galeria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface IGaleriaRepository extends JpaRepository<Galeria,String> {


    @Query("SELECT g FROM Galeria g")
    @EntityGraph(
            attributePaths = {"nombre","idGaleria","fechaDeCreacion","imgPerfil","imgBanner"}
    )
    List<Galeria> findAllWithDetails();


    Page<Galeria> findByfechaDeCreacionAfter(Instant desde, Pageable paginaSolicitada);
}
