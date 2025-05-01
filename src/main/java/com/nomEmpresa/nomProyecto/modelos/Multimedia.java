package com.nomEmpresa.nomProyecto.modelos;

import com.nomEmpresa.nomProyecto.dto.wasabi.modelos.MultimediaDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
public class Multimedia {

    //Getters y Setters
    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String src;



    //Realaciones
    @ManyToOne
    @JoinColumn(name = "id_galeria")
    private Galeria idGaleria;






    public Multimedia() {
    }


    public MultimediaDTO getDTO(){
        return new MultimediaDTO(src);
    }


    public String getSrc() {
        return src;
    }

    public Galeria getIdGaleria() {
        return idGaleria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSrc( String src) {
        this.src = src;
    }

    public void setIdGaleria( Galeria idGaleria) {
        this.idGaleria = idGaleria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Multimedia that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(src, that.src);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, src);
    }
}
