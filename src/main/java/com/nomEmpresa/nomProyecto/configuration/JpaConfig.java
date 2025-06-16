package com.nomEmpresa.nomProyecto.configuration;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.SmartDataSource;
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

    @Value("${datasource.maxPoolSize}")
    private Integer maxPoolSize;

    @Bean
    public DataSource dataSource(){
        HikariDataSource ds = new HikariDataSource(); //Recomendado por spring boot
        ds.setJdbcUrl(url);
        ds.setDriverClassName(driver);
        ds.setUsername(usuario);
        ds.setPassword(contrasenia);
        ds.setMaximumPoolSize(maxPoolSize);

        return ds;
    }


}
