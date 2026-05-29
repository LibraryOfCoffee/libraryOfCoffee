package com.mametosho.domain.model.plan

/**
 * プランを表す集約ルート。
 *
 * Shopifyのプランと1対1で紐づく。定期便（SUBSCRIPTION）と単品（SINGLE）の2種別がある。
 *
 * @property id プランID
 * @property shopifyPlanId ShopifyのプランID。システム内で一意
 * @property label プラン表示名（例: はじめて）
 * @property gramWeight 1種あたりのグラム数（30/60/90）。1以上
 * @property beanQuantity 豆の種類数（3/4/5）。1以上
 * @property price 価格。0以上
 * @property type プラン種別（SUBSCRIPTION / SINGLE）
 * @property isRecommended おすすめバッジ
 */
data class Plan(
    val id: PlanId,
    val shopifyPlanId: ShopifyPlanId,
    val label: String,
    val gramWeight: Int,
    val beanQuantity: Int,
    val price: Int,
    val type: PlanType,
    val isRecommended: Boolean,
) {
    init {
        require(gramWeight >= 1) { "gramWeight must be at least 1" }
        require(beanQuantity >= 1) { "beanQuantity must be at least 1" }
        require(price >= 0) { "price must be non-negative" }
    }
}
