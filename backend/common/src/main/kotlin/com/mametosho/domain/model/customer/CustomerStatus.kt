package com.mametosho.domain.model.customer

/**
 * 顧客ステータス。
 *
 * active → withdrawn の不可逆な遷移のみ許可される。
 */
enum class CustomerStatus {
    ACTIVE,
    WITHDRAWN,
}
