package com.nomEmpresa.nomProyecto.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${exposedHeaders}")
    private String exposedHeaders;

    @Value("${allowedHeaders}")
    private String allowedHeaders;

    @Value("${allowCredentials}")
    private boolean allowCredentials;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica a todos los endpoints
                .allowedOrigins("*") // Permite todos los orígenes (puedes especificar)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite todos los métodos HTTP (GET, POST, etc)
                .allowedHeaders("*")//allowedHeaders, "Access-Control-Request-Method", "Access-Control-Request-Headers") // Permite todos los headers
                .exposedHeaders("*")//exposedHeaders, "Access-Control-Request-Method", "Access-Control-Request-Headers")
                .maxAge(3600);
    }
}