package com.nomEmpresa.nomProyecto.modelos;

import com.nomEmpresa.nomProyecto.dto.modelos.ServicioDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.SqlTypes;

import java.util.*;

@Entity
@Getter
@Setter
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String titulo;

    @Column
    private String tituloExtra;

    @Column
    private String subtitulo;

    @Lob //Large Object
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column
    @ElementCollection
    @CollectionTable(name = "servicio_incluido", joinColumns = @JoinColumn(name = "servicio_id")) //TODO error de tipeo
    private List<String> incluido;

    @Column
    @ElementCollection
    @CollectionTable(name = "servicio_exclusivo", joinColumns = @JoinColumn(name = "servicio_id"))
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

    public Servicio(){}

    public Servicio(ServicioDto dto) {
        titulo = dto.titulo();
        tituloExtra =dto.tituloExtra();
        subtitulo = dto.subtitulo();
        descripcion = dto.descripcion();
        incluido = dto.incluido();
        exclusivo = dto.exclusivo();
        notas = dto.notas();
        tipo = dto.tipo();
        precio = dto.precio();
    }

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
