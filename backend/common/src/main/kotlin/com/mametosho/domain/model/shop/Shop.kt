package com.mametosho.domain.model.shop

import com.mametosho.domain.model.shared.ImageUrl
import java.util.UUID

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

    companion object {
        /**
         * 新しい店舗を生成する。
         *
         * IDはサーバー側でUUIDv4を自動生成する。
         *
         * @param shopifyShopId ShopifyのショップID
         * @param name 店舗名
         * @param introduction 店舗紹介
         * @param particular こだわり
         * @param images 画像情報（種別とURL）のリスト
         * @return 生成された[Shop]
         */
        fun create(
            shopifyShopId: String,
            name: String,
            introduction: String?,
            particular: String?,
            images: List<Pair<String, String>>,
        ): Shop = Shop(
            id = ShopId(UUID.randomUUID().toString()),
            shopifyShopId = ShopifyShopId(shopifyShopId),
            name = name,
            introduction = introduction,
            particular = particular,
            images = images.map { (type, imageUrl) ->
                ShopImage(
                    id = ShopImageId(UUID.randomUUID().toString()),
                    type = ShopImageType.valueOf(type),
                    imageUrl = ImageUrl(imageUrl),
                )
            },
        )
    }
}
