package com.mametosho.domain.model.monthlysubscriptiondetail

import com.mametosho.domain.model.coffeebean.CoffeeBeanId
import com.mametosho.domain.model.customer.CustomerSubscriptionId
import java.time.LocalDate

/**
 * 月次の配送内容を表す集約ルート。
 *
 * 契約者への月次の配送内容を管理する。
 * 顧客の珈琲豆の選択（omakase / selfSelect）と、実際に発送する珈琲豆、発送ステータスを管理する。
 */
data class MonthlySubscriptionDetail(
    val id: MonthlySubscriptionDetailId,
    val customerSubscriptionId: CustomerSubscriptionId,
    val month: LocalDate,
    val selectedType: SelectedType,
    val status: ShippingStatus,
    val choices: List<CoffeeBeanId>,
    val shippingBeans: List<CoffeeBeanId>,
) {
    /**
     * 顧客が珈琲豆を自己選択する。selectedTypeをselfSelectに設定する。
     *
     * @throws IllegalStateException 発送済み（shipped）の場合
     */
    fun selectBeans(coffeeBeanIds: List<CoffeeBeanId>): MonthlySubscriptionDetail {
        check(status == ShippingStatus.UNSHIPPED) { "Cannot select beans for a shipped detail" }
        return copy(selectedType = SelectedType.SELF_SELECT, choices = coffeeBeanIds)
    }

    /**
     * 管理者が発送する珈琲豆を確定する。
     *
     * @throws IllegalStateException 発送済み（shipped）の場合
     */
    fun confirmShippingBeans(coffeeBeanIds: List<CoffeeBeanId>): MonthlySubscriptionDetail {
        check(status == ShippingStatus.UNSHIPPED) { "Cannot confirm shipping beans for a shipped detail" }
        return copy(shippingBeans = coffeeBeanIds)
    }

    /**
     * 発送済みにする。statusをshippedに変更する。
     *
     * @throws IllegalStateException 発送済み（shipped）の場合
     * @throws IllegalStateException 発送する珈琲豆が未確定の場合
     */
    fun ship(): MonthlySubscriptionDetail {
        check(status == ShippingStatus.UNSHIPPED) { "Cannot ship a already shipped detail" }
        check(shippingBeans.isNotEmpty()) { "Shipping beans must be confirmed before shipping" }
        return copy(status = ShippingStatus.SHIPPED)
    }
}
