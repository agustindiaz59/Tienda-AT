package com.nomEmpresa.nomProyecto.modelos;

import com.nomEmpresa.nomProyecto.dto.AdministradorDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
public class Administrador implements UserDetails {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;

    @Id
    @NotEmpty
    private String nombre;

    @Column
    @NotEmpty
    private String contrasenia;

    @Enumerated(EnumType.STRING)
    private Roles rol;

    public Administrador(String nombre, String contrasenia) {
        this.nombre = nombre;
        this.contrasenia = contrasenia;
        this.rol = Roles.ROLE_ADMIN;
    }

    public Administrador(){}

    @Transient
    public AdministradorDTO getDto(){
        return new AdministradorDTO(nombre,contrasenia);
    }

    @PrePersist
    public void generarId() {
        if (nombre == null) {
            nombre = "ADMIN-" + UUID.randomUUID().toString().substring(0,8);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(rol);
    }

    @Override
    public String getPassword() {
        return this.contrasenia;
    }

    @Override
    public String getUsername() {
        return this.nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
