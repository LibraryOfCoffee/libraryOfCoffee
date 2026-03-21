package com.mametosho.admin.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.mametosho.admin.presentation.dto.response.ErrorResponse
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain
import java.time.OffsetDateTime
import javax.crypto.spec.SecretKeySpec

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties::class)
class AdminSecurityConfig(
    private val jwtProperties: JwtProperties,
) {

    private val objectMapper = ObjectMapper().apply {
        findAndRegisterModules()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/api/admin/auth/login").permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                    .requestMatchers("/actuator/**").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.decoder(jwtDecoder())
                }
                oauth2.authenticationEntryPoint { request, response, _ ->
                    val status = HttpStatus.UNAUTHORIZED
                    val errorResponse = ErrorResponse(
                        timestamp = OffsetDateTime.now(),
                        status = status.value(),
                        error = status.reasonPhrase,
                        path = request.requestURI,
                    )
                    response.status = status.value()
                    response.contentType = MediaType.APPLICATION_JSON_VALUE
                    response.writer.write(objectMapper.writeValueAsString(errorResponse))
                }
            }

        return http.build()
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        val secretKey = SecretKeySpec(
            jwtProperties.secretKey.toByteArray(),
            "HmacSHA256",
        )
        return NimbusJwtDecoder.withSecretKey(secretKey).build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
