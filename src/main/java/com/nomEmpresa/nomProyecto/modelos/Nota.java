package com.nomEmpresa.nomProyecto.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "notas")
@Getter
@Setter
@NoArgsConstructor
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String contenido;

    @Column
    private Instant hora;

    @ManyToOne
    @JoinColumn(name = "galeria_id")
    private Galeria galeria;


    public Nota(String contenido, Instant hora) {
        this.hora = hora;
        this.contenido = contenido;
    }

    public Nota(String contenidoNota, Galeria galeria) {
        this.contenido = contenido;
        this.galeria = galeria;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Nota nota = (Nota) o;
        return Objects.equals(contenido, nota.contenido) && Objects.equals(hora, nota.hora);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contenido, hora);
    }
}
