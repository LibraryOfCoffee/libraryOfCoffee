package com.mametosho.admin.application.usecase

import com.mametosho.domain.model.coffeebean.CoffeeBean
import com.mametosho.domain.model.coffeebean.CoffeeBeanId
import com.mametosho.domain.model.coffeebean.ProcessingMethod
import com.mametosho.domain.model.coffeebean.RoastLevel
import com.mametosho.domain.model.coffeebean.ShopifyBeanId
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.repository.CoffeeBeanRepository
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetCoffeeBeanUsecaseTest {

    private val existingBean = CoffeeBean(
        id = CoffeeBeanId("00000000-0000-4000-8000-000000000001"),
        shopId = ShopId("00000000-0000-4000-8000-000000000002"),
        shopifyBeanId = ShopifyBeanId("test-bean-001"),
        name = "テストコーヒー豆",
        description = "テスト説明文",
        origin = "エチオピア",
        farm = "テスト農園",
        roastLevel = RoastLevel.MEDIUM,
        processingMethod = ProcessingMethod.WASHED,
        isSpecialty = true,
        images = emptyList(),
        tastes = emptyList(),
    )

    private fun createUsecase(bean: CoffeeBean? = existingBean): GetCoffeeBeanUsecase {
        val fakeRepository = object : CoffeeBeanRepository {
            override fun save(coffeeBean: CoffeeBean) = Unit
            override fun findById(id: CoffeeBeanId): CoffeeBean? =
                if (id == existingBean.id) bean else null
            override fun deleteById(id: CoffeeBeanId) = Unit
        }
        return GetCoffeeBeanUsecase(fakeRepository)
    }

    @Nested
    inner class 正常系 {
        @Test
        fun `存在するIDでコーヒー豆を取得できる`() {
            val usecase = createUsecase()
            val result = usecase.execute("00000000-0000-4000-8000-000000000001")

            assertEquals(existingBean, result)
        }
    }

    @Nested
    inner class 存在しない場合 {
        @Test
        fun `存在しないIDの場合nullが返る`() {
            val usecase = createUsecase()
            val result = usecase.execute("00000000-0000-4000-8000-999999999999")

            assertNull(result)
        }
    }
}
