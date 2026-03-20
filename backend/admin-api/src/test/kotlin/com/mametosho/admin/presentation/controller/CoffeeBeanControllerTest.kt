package com.mametosho.admin.presentation.controller

import com.mametosho.admin.application.usecase.CreateCoffeeBeanUsecase
import com.mametosho.admin.presentation.dto.request.CreateCoffeeBeanRequest
import com.mametosho.domain.model.coffeebean.CoffeeBean
import com.mametosho.domain.model.coffeebean.CoffeeBeanId
import com.mametosho.domain.model.coffeebean.ProcessingMethod
import com.mametosho.domain.model.coffeebean.RoastLevel
import com.mametosho.domain.model.coffeebean.ShopifyBeanId
import com.mametosho.domain.model.shop.ShopId
import org.junit.jupiter.api.Nested
import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class CoffeeBeanControllerTest {

    private val sampleCoffeeBean = CoffeeBean(
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

    private fun createController(coffeeBean: CoffeeBean = sampleCoffeeBean): CoffeeBeanController {
        val fakeUsecase = object : CreateCoffeeBeanUsecase(
            object : com.mametosho.domain.repository.CoffeeBeanRepository {
                override fun save(coffeeBean: CoffeeBean) = Unit
            },
        ) {
            override fun execute(request: CreateCoffeeBeanRequest): CoffeeBean = coffeeBean
        }
        return CoffeeBeanController(fakeUsecase)
    }

    private fun createRequest(): CreateCoffeeBeanRequest = CreateCoffeeBeanRequest(
        shopId = "00000000-0000-4000-8000-000000000002",
        shopifyBeanId = "test-bean-001",
        name = "テストコーヒー豆",
        description = "テスト説明文",
        origin = "エチオピア",
        farm = "テスト農園",
        roastLevel = "MEDIUM",
        processingMethod = "WASHED",
        isSpecialty = true,
        images = emptyList(),
        tastes = emptyList(),
    )

    @Nested
    inner class 正常系 {
        @Test
        fun `正常にコーヒー豆を登録すると201が返る`() {
            val controller = createController()
            val response = controller.createCoffeeBean(createRequest())

            assertEquals(HttpStatus.CREATED, response.statusCode)
            assertEquals("00000000-0000-4000-8000-000000000001", response.body?.id)
        }
    }
}
