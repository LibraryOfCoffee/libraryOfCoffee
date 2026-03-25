package com.mametosho.infrastructure.storage

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "storage.s3")
data class S3Properties(
    val bucketName: String,
    val region: String,
    val baseUrl: String,
)
