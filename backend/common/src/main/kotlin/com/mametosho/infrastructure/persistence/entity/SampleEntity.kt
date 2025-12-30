package com.mametosho.infrastructure.persistence.entity

import java.time.LocalDateTime

data class SampleEntity(
    val id: Long,
    val name: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)
