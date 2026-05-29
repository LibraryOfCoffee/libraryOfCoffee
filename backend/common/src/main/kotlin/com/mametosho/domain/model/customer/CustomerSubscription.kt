package com.mametosho.domain.model.customer

import com.mametosho.domain.model.subscriptionplan.PlanId
import java.time.LocalDate

/**
 * サブスクリプション契約を表すエンティティ。
 *
 * [Customer]集約に属し、契約のライフサイクル（active → canceled、active ↔ ban）を管理する。
 * canceledになった契約は変更できない。
 *
 * @property id 契約ID
 * @property planId 契約しているプランのID
 * @property status 契約ステータス
 * @property contractPeriod 契約期間
 */
data class CustomerSubscription(
    val id: CustomerSubscriptionId,
    val planId: PlanId,
    val status: SubscriptionStatus,
    val contractPeriod: ContractPeriod,
) {
    /**
     * 解約する。statusをcanceledに変更し、契約終了日を設定する。
     *
     * @param contractedTo 契約終了日
     * @return 解約済みの新しい[CustomerSubscription]
     * @throws IllegalStateException 既にcanceledの場合
     */
    fun cancel(contractedTo: LocalDate): CustomerSubscription {
        check(status != SubscriptionStatus.CANCELED) { "Cannot cancel a already canceled subscription" }
        return copy(
            status = SubscriptionStatus.CANCELED,
            contractPeriod = ContractPeriod(from = contractPeriod.from, to = contractedTo),
        )
    }

    /**
     * 未払い等の理由で管理者が利用を停止（ban）する。
     *
     * @return banされた新しい[CustomerSubscription]
     * @throws IllegalStateException canceledの場合
     */
    fun ban(): CustomerSubscription {
        check(status != SubscriptionStatus.CANCELED) { "Cannot ban a canceled subscription" }
        return copy(status = SubscriptionStatus.BAN)
    }

    /**
     * 支払い確認後、管理者がbanを解除しactiveに戻す。
     *
     * @return activeに戻った新しい[CustomerSubscription]
     * @throws IllegalStateException banでない場合
     */
    fun unban(): CustomerSubscription {
        check(status == SubscriptionStatus.BAN) { "Cannot unban a subscription that is not banned" }
        return copy(status = SubscriptionStatus.ACTIVE)
    }
}
