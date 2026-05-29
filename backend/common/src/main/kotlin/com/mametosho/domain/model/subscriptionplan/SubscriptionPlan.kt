package com.mametosho.domain.model.subscriptionplan

/**
 * サブスクリプションプランを表す集約ルート。
 *
 * Shopifyのサブスクリプションと1対1で紐づく。
 *
 * @property id プランID
 * @property shopifySubscriptionId ShopifyのサブスクリプションID。システム内で一意
 * @property label プラン表示名（例: はじめて）
 * @property gramWeight 1種あたりのグラム数（30/60/90）。1以上
 * @property beanQuantity 豆の種類数（3/4/5）。1以上
 * @property subscriptionPrice 定期便価格。0以上
 * @property singlePrice 単品購入価格。0以上
 * @property isRecommended おすすめバッジ
 */
data class SubscriptionPlan(
    val id: SubscriptionPlanId,
    val shopifySubscriptionId: ShopifySubscriptionId,
    val label: String,
    val gramWeight: Int,
    val beanQuantity: Int,
    val subscriptionPrice: Int,
    val singlePrice: Int,
    val isRecommended: Boolean,
) {
    init {
        require(gramWeight >= 1) { "gramWeight must be at least 1" }
        require(beanQuantity >= 1) { "beanQuantity must be at least 1" }
        require(subscriptionPrice >= 0) { "subscriptionPrice must be non-negative" }
        require(singlePrice >= 0) { "singlePrice must be non-negative" }
    }
}
