package com.nomEmpresa.nomProyecto.repositorio;

import com.nomEmpresa.nomProyecto.modelos.Multimedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IMultimediaRepository extends JpaRepository<Multimedia, Long> {
}
