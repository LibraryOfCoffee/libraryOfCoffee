package com.mametosho.infrastructure.persistence.mybatis.entity

data class ShopListRow(
    val id: String,
    val shopifyShopId: String,
    val name: String,
    val introduction: String?,
    val particular: String?,
    val shopUrl: String,
    val prefecture: String,
    val publishStatus: String,
    val imageId: String?,
    val imageType: String?,
    val imageUrl: String?,
)
