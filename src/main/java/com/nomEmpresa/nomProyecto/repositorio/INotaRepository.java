package com.nomEmpresa.nomProyecto.repositorio;

import com.nomEmpresa.nomProyecto.modelos.Galeria;
import com.nomEmpresa.nomProyecto.modelos.Nota;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface INotaRepository extends JpaRepository<Nota, Long> {

    void deleteByContenidoAndGaleria(String contenido, Galeria galeria);

    Optional<Nota> findByContenidoIgnoreCaseAndGaleria(String contenido, Galeria galeria);

    Page<Nota> findByGaleria(Galeria galeria, Pageable notasPageSolicitada);
}
