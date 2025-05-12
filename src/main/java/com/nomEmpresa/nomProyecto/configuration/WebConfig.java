package com.nomEmpresa.nomProyecto.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Override //Solo se usa en aplicaciones web MVC
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // Aplica a todos los endpoints
//                .allowedOrigins("http:/localhost:5173") // Permite todos los orígenes (puedes especificar)
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite todos los métodos HTTP (GET, POST, etc)
//                .allowedHeaders("*")
//                .allowCredentials(true)
//                .maxAge(3600L);
//    }
}