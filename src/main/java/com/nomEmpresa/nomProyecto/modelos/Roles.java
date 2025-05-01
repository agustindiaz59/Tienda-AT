package com.nomEmpresa.nomProyecto.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;

public enum Roles  implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_USER;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long id;

    Roles(){

    }

    @Override
    public String getAuthority() {
        return name();
    }
}
