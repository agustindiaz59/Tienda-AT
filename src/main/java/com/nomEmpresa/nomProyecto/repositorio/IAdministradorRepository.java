package com.nomEmpresa.nomProyecto.repositorio;

import com.nomEmpresa.nomProyecto.modelos.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAdministradorRepository extends JpaRepository<Administrador,Long> {

    Optional<Administrador> findByNombre(String nombre);

    Boolean existsByNombre(String nombre);

    void deleteByNombre(String nombre);
}
