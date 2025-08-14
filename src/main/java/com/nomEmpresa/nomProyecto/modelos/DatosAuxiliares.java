package com.nomEmpresa.nomProyecto.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
public class DatosAuxiliares {

    @Id
    private Long id;

    @Column
    private String presentacion;

    //Contacto
    @Column
    private String direccion;

    @Column
    private String celular;

    @Column
    private String horario;

    @Column
    private String email;

    //Redes Sociales
    @Column
    private String facebook;

    @Column
    private String instagram;

    @Column
    private String whatsapp;


    // Relaciones
    @OneToMany(mappedBy = "datosAuxiliares", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Servicio> servicios = new LinkedHashSet<>();

    /*
    Presentación
        ...
    Servicios
        ...
    Contacto
        Direccion
        Celular
        Horario
        Email
    Redes sociales
        Facebook
        Instagram
        WhatsApp
     */

    public DatosAuxiliares(){
        this.id = 1L;
    }

    public void agregarServicio(Servicio nuevo){
        this.servicios.add(nuevo);
    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DatosAuxiliares that = (DatosAuxiliares) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
