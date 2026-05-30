package com.mametosho.domain.model.customer

import com.mametosho.domain.model.plan.PlanId
import java.time.LocalDate

/**
 * 顧客を表す集約ルート。
 *
 * Shopifyの顧客と1対1で紐づき、顧客が持つサブスクリプション契約のライフサイクルを管理する。
 * 個人情報（氏名・住所・クレジットカード情報など）はShopify側で管理し、本システムでは保持しない。
 *
 * @property id 顧客ID
 * @property shopifyCustomerId Shopifyの顧客ID。システム内で一意
 * @property status 顧客ステータス
 * @property subscriptions サブスクリプション契約一覧
 */
data class Customer(
    val id: CustomerId,
    val shopifyCustomerId: ShopifyCustomerId,
    val status: CustomerStatus,
    val subscriptions: List<CustomerSubscription>,
) {
    /**
     * 契約を追加する。
     *
     * @param id 新しい契約のID
     * @param planId 契約するプランのID
     * @param contractedFrom 契約開始日
     * @return 契約が追加された新しい[Customer]
     * @throws IllegalStateException 退会済み（withdrawn）の顧客の場合
     * @throws IllegalStateException 同一プランのactive契約が既に存在する場合
     */
    fun addSubscription(
        id: CustomerSubscriptionId,
        planId: PlanId,
        contractedFrom: LocalDate,
    ): Customer {
        check(status != CustomerStatus.WITHDRAWN) { "Cannot add subscription to a withdrawn customer" }
        val hasActiveSamePlan = subscriptions.any {
            it.planId == planId && it.status == SubscriptionStatus.ACTIVE
        }
        check(!hasActiveSamePlan) { "Already has an active subscription for plan: ${planId.value}" }
        val newSubscription = CustomerSubscription(
            id = id,
            planId = planId,
            status = SubscriptionStatus.ACTIVE,
            contractPeriod = ContractPeriod(from = contractedFrom, to = null),
        )
        return copy(subscriptions = subscriptions + newSubscription)
    }

    /**
     * 退会する。statusをwithdrawnに変更し、全契約をcanceledにする。
     *
     * 退会は不可逆。再登録する場合は新しいCustomerとして作成される。
     *
     * @return 退会した新しい[Customer]
     */
    fun withdraw(): Customer {
        val canceledSubscriptions = subscriptions.map { subscription ->
            when (subscription.status) {
                SubscriptionStatus.ACTIVE, SubscriptionStatus.BAN -> subscription.copy(status = SubscriptionStatus.CANCELED)
                SubscriptionStatus.CANCELED -> subscription
            }
        }
        return copy(status = CustomerStatus.WITHDRAWN, subscriptions = canceledSubscriptions)
    }
}
