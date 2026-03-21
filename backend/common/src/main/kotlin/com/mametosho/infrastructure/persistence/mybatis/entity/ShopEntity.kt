package com.mametosho.infrastructure.persistence.mybatis.entity

data class ShopEntity(
    val id: String,
    val shopifyShopId: String,
    val name: String,
    val introduction: String?,
    val particular: String?,
    val shopUrl: String,
)
