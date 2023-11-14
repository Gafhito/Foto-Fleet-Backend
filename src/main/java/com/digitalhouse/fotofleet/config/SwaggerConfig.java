package com.digitalhouse.fotofleet.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info()
                .title("API Foto Fleet")
                .version("1.0")
                .description("Backend del proyecto de egreso del CTD de Digital House, realizado por los estudiantes del Grupo 3 de la Camada 7. \n\n\n" +
                        "Integrantes: \n\n" +
                        "Michael Armesto\n\n" +
                        "Nelson Alirio Pataquiva Alvarez\n\n" +
                        "Marcelo Fullana\n\n" +
                        "Tomas Conesa\n\n" +
                        "Joel Hernan Rincon\n\n" +
                        "Erick Ramirez\n\n" +
                        "Ailen Marcela Gonzalez\n\n" +
                        "Lautaro Dell Olio\n\n" +
                        "Gonzalo Ferrari")
        );
    }
}
