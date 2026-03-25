package com.mametosho.domain.model.customer

import com.mametosho.domain.model.subscriptionplan.SubscriptionPlanId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CustomerSubscriptionTest {

    private fun createSubscription(
        status: SubscriptionStatus = SubscriptionStatus.ACTIVE,
        contractPeriod: ContractPeriod = ContractPeriod(from = LocalDate.of(2025, 1, 1), to = null),
    ): CustomerSubscription = CustomerSubscription(
        id = CustomerSubscriptionId("00000000-0000-4000-8000-00000000000b"),
        subscriptionPlanId = SubscriptionPlanId("00000000-0000-4000-8000-00000000000d"),
        status = status,
        contractPeriod = contractPeriod,
    )

    @Nested
    inner class cancel {

        @Test
        fun `ACTIVE契約をキャンセルできる`() {
            val subscription = createSubscription()
            val canceled = subscription.cancel(LocalDate.of(2025, 6, 30))
            assertEquals(SubscriptionStatus.CANCELED, canceled.status)
            assertEquals(LocalDate.of(2025, 6, 30), canceled.contractPeriod.to)
        }

        @Test
        fun `BAN契約をキャンセルできる`() {
            val subscription = createSubscription(status = SubscriptionStatus.BAN)
            val canceled = subscription.cancel(LocalDate.of(2025, 6, 30))
            assertEquals(SubscriptionStatus.CANCELED, canceled.status)
        }

        @Test
        fun `CANCELED契約をキャンセルすると例外が発生する`() {
            val subscription = createSubscription(status = SubscriptionStatus.CANCELED)
            assertThrows<IllegalStateException> {
                subscription.cancel(LocalDate.of(2025, 6, 30))
            }
        }

        @Test
        fun `キャンセル後のcontractPeriodのfromは変わらない`() {
            val subscription = createSubscription()
            val canceled = subscription.cancel(LocalDate.of(2025, 6, 30))
            assertEquals(LocalDate.of(2025, 1, 1), canceled.contractPeriod.from)
        }
    }

    @Nested
    inner class ban {

        @Test
        fun `ACTIVE契約をBANできる`() {
            val subscription = createSubscription()
            val banned = subscription.ban()
            assertEquals(SubscriptionStatus.BAN, banned.status)
        }

        @Test
        fun `CANCELED契約をBANすると例外が発生する`() {
            val subscription = createSubscription(status = SubscriptionStatus.CANCELED)
            assertThrows<IllegalStateException> {
                subscription.ban()
            }
        }
    }

    @Nested
    inner class unban {

        @Test
        fun `BAN契約をunbanでACTIVEに戻せる`() {
            val subscription = createSubscription(status = SubscriptionStatus.BAN)
            val unbanned = subscription.unban()
            assertEquals(SubscriptionStatus.ACTIVE, unbanned.status)
        }

        @Test
        fun `ACTIVE契約をunbanすると例外が発生する`() {
            val subscription = createSubscription()
            assertThrows<IllegalStateException> {
                subscription.unban()
            }
        }

        @Test
        fun `CANCELED契約をunbanすると例外が発生する`() {
            val subscription = createSubscription(status = SubscriptionStatus.CANCELED)
            assertThrows<IllegalStateException> {
                subscription.unban()
            }
        }
    }
}
