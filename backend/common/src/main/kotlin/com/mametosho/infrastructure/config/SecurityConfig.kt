package com.mametosho.infrastructure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // CSRF無効化（REST APIはステートレスなので不要）
            .csrf { it.disable() }
            // セッション管理をステートレスに
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            // フォームログイン無効化
            .formLogin { it.disable() }
            // HTTP Basic認証無効化
            .httpBasic { it.disable() }
            // 全てのリクエストを許可（必要に応じて後で認証を追加）
            .authorizeHttpRequests { it.anyRequest().permitAll() }

        return http.build()
    }
}
