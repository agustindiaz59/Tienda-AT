package com.nomEmpresa.nomProyecto.modelos;

import com.nomEmpresa.nomProyecto.dto.wasabi.modelos.GaleriaDTO;
import com.nomEmpresa.nomProyecto.dto.wasabi.modelos.MultimediaDTO;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Entity
public class Galeria {

    //Atributos
    @Id
    @Column(name = "id_galeria", length = 12, nullable = false, updatable = false)
    private String idGaleria;

    @Column
    private String nombre;


    @Column
    private LocalDate fechaDeCreacion;


    //Relaciones
    @OneToMany(mappedBy = "idGaleria", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Multimedia> multimedias = new LinkedHashSet<>();




    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "imgPerfil_id")
    private Multimedia imgPerfil;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "img_banner_id")
    private Multimedia imgBanner;


    @Transient
    public GaleriaDTO getDTO(){
        Set<MultimediaDTO> dtoArchivos = multimedias
                .stream()
                .map(Multimedia::getDTO)
                .collect(Collectors.toSet());

        if (imgPerfil == null && imgBanner == null)
            return new GaleriaDTO(
                    idGaleria,
                    dtoArchivos,
                    nombre,
                    fechaDeCreacion
            );
        if (imgBanner == null)
            return new GaleriaDTO(
                    idGaleria,
                    imgPerfil.getDTO(),
                    dtoArchivos,
                    nombre,
                    fechaDeCreacion
            );
        if (imgPerfil == null)
            return new GaleriaDTO(
                    idGaleria,
                    dtoArchivos,
                    nombre,
                    fechaDeCreacion,
                    imgBanner.getDTO()
            );
        return new GaleriaDTO(
                idGaleria,
                imgPerfil.getDTO(),
                imgBanner.getDTO(),
                dtoArchivos,
                nombre,
                fechaDeCreacion
        );
    }




    public Galeria() {
        this.fechaDeCreacion = LocalDate.now();
    }



    public Galeria(String idGaleria) {
        this.idGaleria = idGaleria;
        this.fechaDeCreacion = LocalDate.now();
    }


    public Galeria(String idGaleria, String nombre) {
        this.idGaleria = idGaleria;
        this.nombre = nombre;
        this.fechaDeCreacion = LocalDate.now();
    }



    @PrePersist
    public void generarId() {
        if (idGaleria == null) {
            idGaleria = "AT-" + UUID.randomUUID().toString().substring(0,8);
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






    public String getIdGaleria() {
        return idGaleria;
    }

    public void setIdGaleria(String idGaleria) {
        this.idGaleria = idGaleria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public void setFechaDeCreacion(LocalDate fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }

    public Set<Multimedia> getMultimedias() {
        return multimedias;
    }

    public void setMultimedias(Set<Multimedia> multimedias) {
        multimedias.remove(imgBanner);
        multimedias.remove(imgPerfil);
        this.multimedias = multimedias;
    }

    public Multimedia getImgPerfil() {
        return imgPerfil;
    }

    public void setImgPerfil(Multimedia imgPerfil) {
        this.imgPerfil = imgPerfil;
    }

    public Multimedia getImgBanner() {
        return imgBanner;
    }

    public void setImgBanner(Multimedia imgBanner) {
        this.imgBanner = imgBanner;
    }
}
