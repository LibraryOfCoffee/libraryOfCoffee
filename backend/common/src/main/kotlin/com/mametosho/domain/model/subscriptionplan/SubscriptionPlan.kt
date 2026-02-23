package com.mametosho.domain.model.subscriptionplan

/**
 * サブスクリプションプランを表す集約ルート。
 *
 * Shopifyのサブスクリプションと1対1で紐づく。
 *
 * @property id プランID
 * @property shopifySubscriptionId ShopifyのサブスクリプションID。システム内で一意
 * @property price 価格。0以上
 * @property beanQuantity 1回の配送で届く珈琲豆の数。1以上
 */
data class SubscriptionPlan(
    val id: SubscriptionPlanId,
    val shopifySubscriptionId: ShopifySubscriptionId,
    val price: Int,
    val beanQuantity: Int,
) {
    init {
        require(price >= 0) { "price must be non-negative" }
        require(beanQuantity >= 1) { "beanQuantity must be at least 1" }
    }
}
