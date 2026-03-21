package com.mametosho.infrastructure.storage

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import java.net.URI

@Configuration
@EnableConfigurationProperties(S3Properties::class)
class S3Config {

    @Bean
    @Profile("local", "test")
    fun localS3Client(properties: S3Properties): S3Client =
        S3Client.builder()
            .region(Region.of(properties.region))
            .endpointOverride(URI.create("http://localhost:4566"))
            .serviceConfiguration(
                S3Configuration.builder()
                    .pathStyleAccessEnabled(true)
                    .build(),
            )
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create("test", "test"),
                ),
            )
            .build()

    @Bean
    @Profile("!local & !test")
    fun s3Client(properties: S3Properties): S3Client =
        S3Client.builder()
            .region(Region.of(properties.region))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build()
}
