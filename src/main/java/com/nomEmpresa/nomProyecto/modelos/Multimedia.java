package com.nomEmpresa.nomProyecto.modelos;

import com.nomEmpresa.nomProyecto.dto.modelos.MultimediaDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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

    @Column
    private Instant fechaModificado;



    //Realaciones
    @ManyToOne
    @JoinColumn(name = "id_galeria")
    private Galeria idGaleria;






    public Multimedia() {
        ZoneId argentinaZone = ZoneId.of("America/Argentina/Buenos_Aires"); //Corrijo el UTC a UTC-3(zona horaria en argentina)
        this.fechaModificado = LocalDateTime.now(argentinaZone).toInstant(ZoneOffset.UTC);
    }


    public MultimediaDTO getDTO(){
        return new MultimediaDTO(src, fechaModificado);
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
