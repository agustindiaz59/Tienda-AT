package com.nomEmpresa.nomProyecto.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


@Entity
@Getter
@Setter
@Component
public class Galeria {

    //Atributos
    @Id
    @Column(name = "id_galeria", length = 12, nullable = false, updatable = false)
    private String idGaleria;

    @Column
    private String nombre;


    @Column
    private Instant fechaDeCreacion;


    //Relaciones
    @OneToMany(mappedBy = "idGaleria", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Multimedia> multimedias = new LinkedHashSet<>();





    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "imgPerfil_id")
    private Multimedia imgPerfil;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "img_banner_id")
    private Multimedia imgBanner;

    @OneToMany(mappedBy = "galeria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nota> notas = new ArrayList<>();



    public Galeria() {
        this.fechaDeCreacion = Instant.now();
    }



    public Galeria(String idGaleria) {
        this.idGaleria = idGaleria;
        this.fechaDeCreacion = Instant.now();
    }


    public Galeria(String idGaleria, String nombre) {
        this.idGaleria = idGaleria;
        this.nombre = nombre;
        this.fechaDeCreacion = Instant.now();
    }



    @PrePersist
    public void generarId() {
        if (idGaleria == null) {
            idGaleria = "AT-" + UUID.randomUUID().toString().substring(0,5).toUpperCase(Locale.ENGLISH);
        }
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Galeria galeria)) return false;
        return Objects.equals(idGaleria, galeria.idGaleria);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idGaleria);
    }




    public void setMultimedias(Set<Multimedia> multimedias) {
        multimedias.remove(imgBanner);
        multimedias.remove(imgPerfil);
        this.multimedias = multimedias;
    }

}
