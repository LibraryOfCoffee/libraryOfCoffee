package com.mametosho.cs.config

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
                    .title("CS API")
                    .description("Library of Coffee - Customer Service API")
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
            .components(
                Components().apply {
                    HMAC_HEADERS.forEach { (header, description) ->
                        addSecuritySchemes(header, apiKeyHeader(header, description))
                    }
                }
            )
            .addSecurityItem(
                SecurityRequirement().apply {
                    HMAC_HEADERS.forEach { (header, _) -> addList(header) }
                }
            )
    }

    private fun apiKeyHeader(headerName: String, description: String): SecurityScheme =
        SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .`in`(SecurityScheme.In.HEADER)
            .name(headerName)
            .description(description)

    companion object {
        // スキーム名はヘッダ名と同一にし、各HMACヘッダを1箇所だけで宣言する。
        private val HMAC_HEADERS = listOf(
            "X-Client-Id" to "クライアント識別子",
            "X-Timestamp" to "UNIXエポック秒",
            "X-Signature" to "HMAC-SHA256署名(hex)",
        )
    }
}
