package com.mametosho.admin.presentation.dto.response

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
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals

class CoffeeBeanResponseTest {

    private fun createCoffeeBean(): CoffeeBean = CoffeeBean(
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
                id = CoffeeBeanImageId("00000000-0000-4000-8000-000000000010"),
                type = CoffeeBeanImageType.MAIN,
                imageUrl = ImageUrl("https://example.com/bean.jpg"),
            ),
        ),
        tastes = listOf(
            CoffeeBeanTaste(
                id = CoffeeBeanTasteId("00000000-0000-4000-8000-000000000020"),
                tasteId = TasteId("00000000-0000-4000-8000-000000000041"),
                evaluationValue = 3,
            ),
        ),
    )

    @Nested
    inner class 正常系変換 {
        @Test
        fun `正常にCoffeeBeanからCoffeeBeanResponseに変換できる`() {
            val response = CoffeeBeanResponse.from(createCoffeeBean())
            assertEquals("00000000-0000-4000-8000-000000000001", response.id)
        }
    }
}
