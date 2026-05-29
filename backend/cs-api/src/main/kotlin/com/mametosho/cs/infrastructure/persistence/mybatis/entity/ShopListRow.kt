package com.mametosho.cs.infrastructure.persistence.mybatis.entity

data class ShopListRow(
    val id: String,           // 主テーブル → non-null
    val name: String,
    val introduction: String,
    val shopUrl: String,
    val prefecture: String,
    val logoImageUrl: String, // INNER JOIN (shop_images type='LOGO') → non-null
)
