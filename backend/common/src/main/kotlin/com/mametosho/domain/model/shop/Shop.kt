package com.mametosho.domain.model.shop

/**
 * 店舗を表す集約ルート。
 *
 * 珈琲豆を提供する店舗の情報を管理する。Shopifyのショップと1対1で紐づく。
 *
 * @property id 店舗ID
 * @property shopifyShopId ShopifyのショップID。システム内で一意
 * @property name 店舗名
 * @property introduction 店舗紹介
 * @property particular こだわり
 * @property images 店舗画像一覧
 */
data class Shop(
    val id: ShopId,
    val shopifyShopId: ShopifyShopId,
    val name: String,
    val introduction: String?,
    val particular: String?,
    val images: List<ShopImage>,
) {
    init {
        require(name.isNotBlank()) { "name must not be blank" }
    }
}
