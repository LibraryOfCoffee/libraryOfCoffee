package com.mametosho.domain.model.monthlysubscriptiondetail

import com.mametosho.domain.model.coffeebean.CoffeeBeanId
import com.mametosho.domain.model.customer.CustomerSubscriptionId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class MonthlySubscriptionDetailTest {

    private fun createDetail(
        selectedType: SelectedType = SelectedType.OMAKASE,
        status: ShippingStatus = ShippingStatus.UNSHIPPED,
        choices: List<CoffeeBeanId> = emptyList(),
        shippingBeans: List<CoffeeBeanId> = emptyList(),
    ): MonthlySubscriptionDetail = MonthlySubscriptionDetail(
        id = MonthlySubscriptionDetailId("00000000-0000-4000-8000-000000000013"),
        customerSubscriptionId = CustomerSubscriptionId("00000000-0000-4000-8000-00000000000b"),
        month = LocalDate.of(2025, 7, 1),
        selectedType = selectedType,
        status = status,
        choices = choices,
        shippingBeans = shippingBeans,
    )

    @Nested
    inner class 生成テスト {

        @Test
        fun `正常にMonthlySubscriptionDetailを生成できる`() {
            val detail = createDetail()
            assertEquals(SelectedType.OMAKASE, detail.selectedType)
            assertEquals(ShippingStatus.UNSHIPPED, detail.status)
        }
    }

    @Nested
    inner class selectBeans {

        @Test
        fun `珈琲豆を自己選択できる`() {
            val detail = createDetail()
            val beanIds = listOf(
                CoffeeBeanId("00000000-0000-4000-8000-000000000001"),
                CoffeeBeanId("00000000-0000-4000-8000-000000000002"),
            )
            val updated = detail.selectBeans(beanIds)
            assertEquals(SelectedType.SELF_SELECT, updated.selectedType)
            assertEquals(2, updated.choices.size)
        }

        @Test
        fun `発送済みの場合は珈琲豆を選択すると例外が発生する`() {
            val detail = createDetail(status = ShippingStatus.SHIPPED)
            assertThrows<IllegalStateException> {
                detail.selectBeans(listOf(CoffeeBeanId("00000000-0000-4000-8000-000000000001")))
            }
        }
    }

    @Nested
    inner class confirmShippingBeans {

        @Test
        fun `発送する珈琲豆を確定できる`() {
            val detail = createDetail()
            val beanIds = listOf(
                CoffeeBeanId("00000000-0000-4000-8000-000000000001"),
                CoffeeBeanId("00000000-0000-4000-8000-000000000002"),
            )
            val updated = detail.confirmShippingBeans(beanIds)
            assertEquals(2, updated.shippingBeans.size)
        }

        @Test
        fun `発送済みの場合は発送豆を確定すると例外が発生する`() {
            val detail = createDetail(status = ShippingStatus.SHIPPED)
            assertThrows<IllegalStateException> {
                detail.confirmShippingBeans(listOf(CoffeeBeanId("00000000-0000-4000-8000-000000000001")))
            }
        }
    }

    @Nested
    inner class ship {

        @Test
        fun `発送済みにできる`() {
            val beanIds = listOf(CoffeeBeanId("00000000-0000-4000-8000-000000000001"))
            val detail = createDetail(shippingBeans = beanIds)
            val shipped = detail.ship()
            assertEquals(ShippingStatus.SHIPPED, shipped.status)
        }

        @Test
        fun `発送済みの場合は再度発送済みにすると例外が発生する`() {
            val detail = createDetail(
                status = ShippingStatus.SHIPPED,
                shippingBeans = listOf(CoffeeBeanId("00000000-0000-4000-8000-000000000001")),
            )
            assertThrows<IllegalStateException> {
                detail.ship()
            }
        }

        @Test
        fun `発送豆が未確定の場合は発送済みにすると例外が発生する`() {
            val detail = createDetail()
            assertThrows<IllegalStateException> {
                detail.ship()
            }
        }
    }
}
