package com.nomEmpresa.nomProyecto.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class OpenApiConfig {

    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Mi API REST")
                        .version("1.0")
                        .description("Documentación de mi API REST con SpringDoc OpenAPI 3"));
    }

}
