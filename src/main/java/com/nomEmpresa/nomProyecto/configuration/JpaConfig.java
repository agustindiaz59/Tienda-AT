package com.nomEmpresa.nomProyecto.configuration;

import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.sql.DataSource;

@Configuration
@EntityScan(basePackages = "com.nomEmpresa.nomProyecto.modelos")
public class JpaConfig {

    @Value("${datasource.driver}")
    private String driver;

    @Value("${datasource.url}")
    private String url;

    @Value("${datasource.user}")
    private String usuario;

    @Value("${datasource.password}")
    private String contrasenia;

    @Bean
    public DataSource dataSource(){
        return DataSourceBuilder
                .create()
                .driverClassName(driver)
                .username(usuario)
                .password(contrasenia)
                .url(url)
                .build();
    }


    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

}
