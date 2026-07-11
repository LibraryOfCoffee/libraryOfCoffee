package com.mametosho.admin.test

import com.mametosho.domain.service.ImageStorageService
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class FakeImageStorageConfig {
    @Bean
    @Primary
    fun imageStorageService(): ImageStorageService = FakeImageStorageService
}
