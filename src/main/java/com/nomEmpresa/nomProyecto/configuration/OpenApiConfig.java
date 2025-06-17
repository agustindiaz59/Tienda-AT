package com.nomEmpresa.nomProyecto.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("API REST para AT eventos")
                        .version("1.0")
                        .description("Esta api conecta la base de datos de los eventos, con un contenedor de wasabi que almacena la multimedia necesaria"))
                ;
    }

}
