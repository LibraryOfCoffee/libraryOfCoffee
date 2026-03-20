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
@Suppress("MagicNumber")
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
        require(name.length <= 255) { "name must be at most 255 characters, but was ${name.length}" }
        introduction?.let {
            require(it.isNotBlank()) { "introduction must not be blank" }
            require(it.length <= 10000) { "introduction must be at most 10000 characters, but was ${it.length}" }
        }
        particular?.let {
            require(it.isNotBlank()) { "particular must not be blank" }
            require(it.length <= 10000) { "particular must be at most 10000 characters, but was ${it.length}" }
        }
    }
}
