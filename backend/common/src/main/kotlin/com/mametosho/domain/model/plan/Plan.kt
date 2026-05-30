package com.mametosho.domain.model.plan

/**
 * プランを表す集約ルート。
 *
 * Shopifyのプランと1対1で紐づく。定期便（SUBSCRIPTION）と単品（SINGLE）の2種別がある。
 *
 * @property shopifyPlanId システム内で一意
 * @property label プラン表示名（例: はじめて）
 * @property gramWeight 30 / 60 / 90 のいずれか
 * @property beanQuantity 3 / 4 / 5 のいずれか
 * @property price 0以上
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
        require(gramWeight in VALID_GRAM_WEIGHTS) { "gramWeight must be 30, 60, or 90, but was $gramWeight" }
        require(beanQuantity in VALID_BEAN_QUANTITIES) { "beanQuantity must be 3, 4, or 5, but was $beanQuantity" }
        require(price >= 0) { "price must be non-negative" }
    }

    companion object {
        @Suppress("MagicNumber")
        val VALID_GRAM_WEIGHTS = setOf(30, 60, 90)
        @Suppress("MagicNumber")
        val VALID_BEAN_QUANTITIES = setOf(3, 4, 5)
    }
}
