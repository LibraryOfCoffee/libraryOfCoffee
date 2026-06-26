package com.mametosho.cs.config

import com.mametosho.cs.infrastructure.security.HmacSignatureFilter
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.intercept.AuthorizationFilter

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(HmacProperties::class)
class SecurityConfig {

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        hmacSignatureFilter: ObjectProvider<HmacSignatureFilter>,
    ): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .authorizeHttpRequests { it.anyRequest().permitAll() }

        // openapiプロファイルではフィルタBeanが存在しないためスキップ(swagger自動生成を妨げない)。
        hmacSignatureFilter.ifAvailable?.let {
            http.addFilterBefore(it, AuthorizationFilter::class.java)
        }

        return http.build()
    }
}
