package com.mametosho.admin.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Admin API")
                    .description("Library of Coffee - Admin API")
                    .version("1.0.0")
                    .contact(
                        Contact()
                            .name("Library of Coffee")
                    )
            )
            .servers(
                listOf(
                    Server().url("/").description("Current Server")
                )
            )
            .addSecurityItem(SecurityRequirement().addList("Bearer"))
            .components(
                Components().addSecuritySchemes(
                    "Bearer",
                    SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("JWTアクセストークン"),
                )
            )
    }
}
