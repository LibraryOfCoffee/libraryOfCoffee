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

    /**
     * プラン情報を更新する。
     *
     * PlanIdは変更せず、それ以外の項目を置換する。
     *
     * @param shopifyPlanId ShopifyのプランID
     * @param label プラン表示名
     * @param gramWeight 1種あたりのグラム数
     * @param beanQuantity 豆の種類数
     * @param price 価格
     * @param type プラン種別
     * @param isRecommended おすすめバッジ
     * @return 更新された[Plan]
     */
    fun update(
        shopifyPlanId: String,
        label: String,
        gramWeight: Int,
        beanQuantity: Int,
        price: Int,
        type: PlanType,
        isRecommended: Boolean,
    ): Plan = Plan(
        id = this.id,
        shopifyPlanId = ShopifyPlanId(shopifyPlanId),
        label = label,
        gramWeight = gramWeight,
        beanQuantity = beanQuantity,
        price = price,
        type = type,
        isRecommended = isRecommended,
    )

    companion object {
        @Suppress("MagicNumber")
        val VALID_GRAM_WEIGHTS = setOf(30, 60, 90)
        @Suppress("MagicNumber")
        val VALID_BEAN_QUANTITIES = setOf(3, 4, 5)
    }
}
