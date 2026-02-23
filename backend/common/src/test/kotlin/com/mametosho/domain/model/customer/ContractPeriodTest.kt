package com.mametosho.domain.model.customer

import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ContractPeriodTest {

    @Test
    fun `正常にContractPeriodを生成できる`() {
        val period = ContractPeriod(
            from = LocalDate.of(2025, 1, 1),
            to = LocalDate.of(2025, 12, 31),
        )
        assertEquals(LocalDate.of(2025, 1, 1), period.from)
        assertEquals(LocalDate.of(2025, 12, 31), period.to)
    }

    @Test
    fun `toがnullでも生成できる`() {
        val period = ContractPeriod(
            from = LocalDate.of(2025, 1, 1),
            to = null,
        )
        assertNull(period.to)
    }

    @Test
    fun `toがfromより前の場合は例外が発生する`() {
        assertThrows<IllegalArgumentException> {
            ContractPeriod(
                from = LocalDate.of(2025, 6, 1),
                to = LocalDate.of(2025, 1, 1),
            )
        }
    }

    @Test
    fun `toがfromと同じ日でも生成できる`() {
        val period = ContractPeriod(
            from = LocalDate.of(2025, 1, 1),
            to = LocalDate.of(2025, 1, 1),
        )
        assertEquals(period.from, period.to)
    }
}
