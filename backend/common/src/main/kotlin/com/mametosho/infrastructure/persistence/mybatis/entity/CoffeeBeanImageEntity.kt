package com.mametosho.infrastructure.persistence.mybatis.entity

data class CoffeeBeanImageEntity(
    val id: String,
    val coffeeBeanId: String,
    val type: String,
    val imageUrl: String,
)
