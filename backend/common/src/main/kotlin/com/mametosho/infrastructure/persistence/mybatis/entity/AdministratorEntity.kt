package com.mametosho.infrastructure.persistence.mybatis.entity

data class AdministratorEntity(
    val id: String,
    val email: String,
    val hashedPassword: String,
    val role: String,
)
