package com.mametosho.domain.model.plan

/**
 * プランを表す集約ルート。
 *
 * Shopifyのプランと1対1で紐づく。定期便（SUBSCRIPTION）と単品（SINGLE）の2種別がある。
 *
 * @property id プランID
 * @property shopifyPlanId ShopifyのプランID。システム内で一意
 * @property label プラン表示名（例: はじめて）
 * @property gramWeight 1種あたりのグラム数（30 / 60 / 90 のいずれか）
 * @property beanQuantity 豆の種類数（3 / 4 / 5 のいずれか）
 * @property price 価格。0以上
 * @property type プラン種別（SUBSCRIPTION / SINGLE）
 * @property isRecommended おすすめバッジ
 */
@Suppress("MagicNumber")
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
        require(gramWeight in setOf(30, 60, 90)) { "gramWeight must be 30, 60, or 90, but was $gramWeight" }
        require(beanQuantity in setOf(3, 4, 5)) { "beanQuantity must be 3, 4, or 5, but was $beanQuantity" }
        require(price >= 0) { "price must be non-negative" }
    }
}
