package com.mametosho.domain.model.coffeebean

import com.mametosho.domain.model.shop.ShopId

/**
 * コーヒー豆を表す集約ルート。
 *
 * ショップに属するコーヒー豆の情報を管理する。Shopifyの商品と1対1で紐づく。
 *
 * @property id コーヒー豆ID
 * @property shopId 所属するショップのID
 * @property shopifyBeanId Shopifyの商品ID
 * @property name 豆の名前
 * @property description 説明
 * @property origin 産地
 * @property farm 農園名。不明な場合はnull
 * @property roastLevel 焙煎度
 * @property processingMethod 精製方法
 * @property images 画像一覧
 * @property tastes テイスト評価一覧。同一TasteIdの重複は不可
 */
data class CoffeeBean(
    val id: CoffeeBeanId,
    val shopId: ShopId,
    val shopifyBeanId: ShopifyBeanId,
    val name: String,
    val description: String,
    val origin: String,
    val farm: String?,
    val roastLevel: RoastLevel,
    val processingMethod: ProcessingMethod,
    val images: List<CoffeeBeanImage>,
    val tastes: List<CoffeeBeanTaste>,
) {
    init {
        val duplicateTasteIds = tastes.groupBy { it.tasteId }.filter { it.value.size > 1 }.keys
        require(duplicateTasteIds.isEmpty()) {
            "Duplicate tasteId is not allowed: $duplicateTasteIds"
        }
    }
}
