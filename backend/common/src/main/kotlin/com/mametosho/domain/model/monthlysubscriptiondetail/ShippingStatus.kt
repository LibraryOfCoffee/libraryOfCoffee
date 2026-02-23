package com.mametosho.domain.model.monthlysubscriptiondetail

/**
 * 発送ステータス。
 *
 * unshipped → shipped の不可逆な遷移のみ許可される。
 */
enum class ShippingStatus {
    UNSHIPPED,
    SHIPPED,
}
