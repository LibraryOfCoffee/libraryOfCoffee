package com.mametosho.domain.model.customer

import java.time.LocalDate

/**
 * サブスクリプションの契約期間を表す値オブジェクト。
 *
 * @property from 契約開始日。引き落とし完了日
 * @property to 契約終了日。解約時にのみ設定される。設定される場合は[from]より前であってはならない
 */
data class ContractPeriod(
    val from: LocalDate,
    val to: LocalDate?,
) {
    init {
        if (to != null) {
            require(!to.isBefore(from)) { "contractedTo must not be before contractedFrom" }
        }
    }
}
