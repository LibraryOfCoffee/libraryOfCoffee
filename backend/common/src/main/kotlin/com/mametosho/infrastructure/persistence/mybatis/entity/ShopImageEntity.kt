package com.mametosho.infrastructure.persistence.mybatis.entity

data class ShopImageEntity(
    val id: String,
    val shopId: String,
    val type: String,
    val imageUrl: String,
)
