package com.mametosho.domain.model.customer

import com.mametosho.domain.model.plan.PlanId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CustomerTest {

    private fun createCustomer(
        status: CustomerStatus = CustomerStatus.ACTIVE,
        subscriptions: List<CustomerSubscription> = emptyList(),
    ): Customer = Customer(
        id = CustomerId("00000000-0000-4000-8000-00000000000a"),
        shopifyCustomerId = ShopifyCustomerId("shopify-customer-1"),
        status = status,
        subscriptions = subscriptions,
    )

    private fun createSubscription(
        id: String = "00000000-0000-4000-8000-00000000000b",
        planId: String = "00000000-0000-4000-8000-00000000000d",
        status: SubscriptionStatus = SubscriptionStatus.ACTIVE,
    ): CustomerSubscription = CustomerSubscription(
        id = CustomerSubscriptionId(id),
        planId = PlanId(planId),
        status = status,
        contractPeriod = ContractPeriod(from = LocalDate.of(2025, 1, 1), to = null),
    )

    @Nested
    inner class 生成テスト {

        @Test
        fun `正常にCustomerを生成できる`() {
            val customer = createCustomer()
            assertEquals(CustomerStatus.ACTIVE, customer.status)
            assertTrue(customer.subscriptions.isEmpty())
        }
    }

    @Nested
    inner class addSubscription {

        @Test
        fun `契約を追加できる`() {
            val customer = createCustomer()
            val updated = customer.addSubscription(
                id = CustomerSubscriptionId("00000000-0000-4000-8000-00000000000b"),
                planId = PlanId("00000000-0000-4000-8000-00000000000d"),
                contractedFrom = LocalDate.of(2025, 1, 1),
            )
            assertEquals(1, updated.subscriptions.size)
            assertEquals(SubscriptionStatus.ACTIVE, updated.subscriptions[0].status)
        }

        @Test
        fun `退会済み顧客に契約を追加すると例外が発生する`() {
            val customer = createCustomer(status = CustomerStatus.WITHDRAWN)
            assertThrows<IllegalStateException> {
                customer.addSubscription(
                    id = CustomerSubscriptionId("00000000-0000-4000-8000-00000000000b"),
                    planId = PlanId("00000000-0000-4000-8000-00000000000d"),
                    contractedFrom = LocalDate.of(2025, 1, 1),
                )
            }
        }

        @Test
        fun `同一プランのACTIVE契約がある場合は追加すると例外が発生する`() {
            val planId = "00000000-0000-4000-8000-00000000000d"
            val customer = createCustomer(
                subscriptions = listOf(createSubscription(planId = planId)),
            )
            assertThrows<IllegalStateException> {
                customer.addSubscription(
                    id = CustomerSubscriptionId("00000000-0000-4000-8000-00000000000c"),
                    planId = PlanId(planId),
                    contractedFrom = LocalDate.of(2025, 6, 1),
                )
            }
        }

        @Test
        fun `同一プランでもCANCELED契約なら新しい契約を追加できる`() {
            val planId = "00000000-0000-4000-8000-00000000000d"
            val customer = createCustomer(
                subscriptions = listOf(createSubscription(planId = planId, status = SubscriptionStatus.CANCELED)),
            )
            val updated = customer.addSubscription(
                id = CustomerSubscriptionId("00000000-0000-4000-8000-00000000000c"),
                planId = PlanId(planId),
                contractedFrom = LocalDate.of(2025, 6, 1),
            )
            assertEquals(2, updated.subscriptions.size)
        }

        @Test
        fun `異なるプランなら複数の契約を追加できる`() {
            val customer = createCustomer(
                subscriptions = listOf(createSubscription(planId = "00000000-0000-4000-8000-00000000000d")),
            )
            val updated = customer.addSubscription(
                id = CustomerSubscriptionId("00000000-0000-4000-8000-00000000000c"),
                planId = PlanId("00000000-0000-4000-8000-00000000000e"),
                contractedFrom = LocalDate.of(2025, 6, 1),
            )
            assertEquals(2, updated.subscriptions.size)
        }
    }

    @Nested
    inner class withdraw {

        @Test
        fun `退会するとステータスがWITHDRAWNになる`() {
            val customer = createCustomer()
            val withdrawn = customer.withdraw()
            assertEquals(CustomerStatus.WITHDRAWN, withdrawn.status)
        }

        @Test
        fun `退会すると全ACTIVE契約がCANCELEDになる`() {
            val customer = createCustomer(
                subscriptions = listOf(
                    createSubscription(id = "00000000-0000-4000-8000-00000000000b", planId = "00000000-0000-4000-8000-00000000000d"),
                    createSubscription(id = "00000000-0000-4000-8000-00000000000c", planId = "00000000-0000-4000-8000-00000000000e"),
                ),
            )
            val withdrawn = customer.withdraw()
            assertTrue(withdrawn.subscriptions.all { it.status == SubscriptionStatus.CANCELED })
        }

        @Test
        fun `退会すると全BAN契約もCANCELEDになる`() {
            val customer = createCustomer(
                subscriptions = listOf(
                    createSubscription(id = "00000000-0000-4000-8000-00000000000b", status = SubscriptionStatus.BAN),
                ),
            )
            val withdrawn = customer.withdraw()
            assertEquals(SubscriptionStatus.CANCELED, withdrawn.subscriptions[0].status)
        }

        @Test
        fun `退会時に既にCANCELEDの契約はそのまま`() {
            val customer = createCustomer(
                subscriptions = listOf(
                    createSubscription(id = "00000000-0000-4000-8000-00000000000b", status = SubscriptionStatus.CANCELED),
                ),
            )
            val withdrawn = customer.withdraw()
            assertEquals(SubscriptionStatus.CANCELED, withdrawn.subscriptions[0].status)
        }
    }
}
