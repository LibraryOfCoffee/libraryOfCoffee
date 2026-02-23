package com.mametosho.domain.model.customer

/**
 * 契約ステータス。
 *
 * 状態遷移: active → canceled、active ↔ ban、ban → canceled。
 * canceledからの遷移は不可。
 */
enum class SubscriptionStatus {
    ACTIVE,
    CANCELED,
    BAN,
}
