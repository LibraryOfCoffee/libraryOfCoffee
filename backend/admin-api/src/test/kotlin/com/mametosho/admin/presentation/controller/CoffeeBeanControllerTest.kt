package com.mametosho.admin.presentation.controller

import com.mametosho.admin.application.usecase.CreateCoffeeBeanUsecase
import com.mametosho.admin.presentation.dto.request.CreateCoffeeBeanRequest
import com.mametosho.domain.model.coffeebean.CoffeeBean
import com.mametosho.domain.model.coffeebean.CoffeeBeanId
import com.mametosho.domain.model.coffeebean.CoffeeBeanImage
import com.mametosho.domain.model.coffeebean.CoffeeBeanImageId
import com.mametosho.domain.model.coffeebean.CoffeeBeanImageType
import com.mametosho.domain.model.coffeebean.CoffeeBeanTaste
import com.mametosho.domain.model.coffeebean.CoffeeBeanTasteId
import com.mametosho.domain.model.coffeebean.ProcessingMethod
import com.mametosho.domain.model.coffeebean.RoastLevel
import com.mametosho.domain.model.coffeebean.ShopifyBeanId
import com.mametosho.domain.model.shared.ImageUrl
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.model.taste.TasteId
import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
        images = listOf(
            CoffeeBeanImage(
                id = CoffeeBeanImageId("00000000-0000-4000-8000-000000000011"),
                type = CoffeeBeanImageType.MAIN,
                imageUrl = ImageUrl("https://example.com/bean.png"),
            ),
        ),
        tastes = listOf(
            CoffeeBeanTaste(
                id = CoffeeBeanTasteId("00000000-0000-4000-8000-000000000021"),
                tasteId = TasteId("00000000-0000-4000-8000-000000000031"),
                evaluationValue = 3,
            ),
        ),
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
        images = listOf(
            CreateCoffeeBeanRequest.ImageRequest(type = "MAIN", imageUrl = "https://example.com/bean.png"),
        ),
        tastes = listOf(
            CreateCoffeeBeanRequest.TasteRequest(
                tasteId = "00000000-0000-4000-8000-000000000031",
                evaluationValue = 3,
            ),
        ),
    )

    @Test
    fun `正常にコーヒー豆を登録すると201が返る`() {
        val controller = createController()
        val response = controller.createCoffeeBean(createRequest())

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals("00000000-0000-4000-8000-000000000001", response.body?.id)
        assertEquals("00000000-0000-4000-8000-000000000002", response.body?.shopId)
        assertEquals("test-bean-001", response.body?.shopifyBeanId)
        assertEquals("テストコーヒー豆", response.body?.name)
        assertEquals("テスト説明文", response.body?.description)
        assertEquals("エチオピア", response.body?.origin)
        assertEquals("テスト農園", response.body?.farm)
        assertEquals("MEDIUM", response.body?.roastLevel)
        assertEquals("WASHED", response.body?.processingMethod)
        assertTrue(response.body?.isSpecialty == true)
    }

    @Test
    fun `レスポンスに画像情報が含まれる`() {
        val controller = createController()
        val response = controller.createCoffeeBean(createRequest())

        assertEquals(1, response.body?.images?.size)
        assertEquals("00000000-0000-4000-8000-000000000011", response.body?.images?.get(0)?.id)
        assertEquals("MAIN", response.body?.images?.get(0)?.type)
        assertEquals("https://example.com/bean.png", response.body?.images?.get(0)?.imageUrl)
    }

    @Test
    fun `レスポンスにテイスト情報が含まれる`() {
        val controller = createController()
        val response = controller.createCoffeeBean(createRequest())

        assertEquals(1, response.body?.tastes?.size)
        assertEquals("00000000-0000-4000-8000-000000000021", response.body?.tastes?.get(0)?.id)
        assertEquals("00000000-0000-4000-8000-000000000031", response.body?.tastes?.get(0)?.tasteId)
        assertEquals(3, response.body?.tastes?.get(0)?.evaluationValue)
    }
}
