package com.mametosho.admin.application.query.result

data class ShopListResult(
    val id: String,
    val shopifyShopId: String,
    val name: String,
    val introduction: String?,
    val particular: String?,
    val shopUrl: String?,
)
