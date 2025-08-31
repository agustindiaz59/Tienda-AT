package com.nomEmpresa.nomProyecto.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column()
    private String titulo;

    @Column
    private String subtitulo;

    @Column
    private String Descripcion;

    @Column
    private List<String> incluido;

    @Column
    private List<String> exclusivo;

    @Column
    private String notas;

    @Column
    private String tipo;

    @Column
    private Float precio;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "datos_auxiliares_id")
    private DatosAuxiliares datosAuxiliares;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Servicio servicio = (Servicio) o;
        return Objects.equals(getId(), servicio.getId()) && Objects.equals(getTitulo(), servicio.getTitulo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitulo());
    }
}
