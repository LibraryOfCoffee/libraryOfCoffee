package com.mametosho.admin.application.usecase

import com.mametosho.admin.presentation.dto.request.UpdateCoffeeBeanRequest
import com.mametosho.domain.model.coffeebean.CoffeeBean
import com.mametosho.domain.model.coffeebean.CoffeeBeanId
import com.mametosho.domain.model.coffeebean.CoffeeBeanImageType
import com.mametosho.domain.model.coffeebean.ProcessingMethod
import com.mametosho.domain.model.coffeebean.RoastLevel
import com.mametosho.domain.model.coffeebean.ShopifyBeanId
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.repository.CoffeeBeanRepository
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UpdateCoffeeBeanUsecaseTest {

    private val savedBeans = mutableListOf<CoffeeBean>()

    private val existingBean = CoffeeBean.create(
        shopId = "00000000-0000-4000-8000-000000000002",
        shopifyBeanId = "original-bean-001",
        name = "元のコーヒー豆",
        description = "元の説明文",
        origin = "エチオピア",
        farm = "元の農園",
        roastLevel = "LIGHT",
        processingMethod = "WASHED",
        isSpecialty = false,
        images = listOf("MAIN" to "https://example.com/original.png"),
        tastes = listOf("00000000-0000-4000-8000-000000000003" to 3),
    )

    private val fakeRepository = object : CoffeeBeanRepository {
        override fun save(coffeeBean: CoffeeBean) {
            savedBeans.add(coffeeBean)
        }

        override fun findById(id: CoffeeBeanId): CoffeeBean? {
            return if (id == existingBean.id) existingBean else null
        }

        override fun deleteById(id: CoffeeBeanId) = Unit
    }

    private val usecase = UpdateCoffeeBeanUsecase(fakeRepository)

    private fun createRequest(
        shopifyBeanId: String = "updated-bean-001",
        name: String = "更新後コーヒー豆",
        description: String = "更新後説明文",
        origin: String = "ブラジル",
        farm: String? = "更新後農園",
        roastLevel: String = "FRENCH",
        processingMethod: String = "NATURAL",
        isSpecialty: Boolean = true,
        images: List<UpdateCoffeeBeanRequest.ImageRequest> = listOf(
            UpdateCoffeeBeanRequest.ImageRequest(type = "MAIN", imageUrl = "https://example.com/updated.png"),
        ),
        tastes: List<UpdateCoffeeBeanRequest.TasteRequest> = listOf(
            UpdateCoffeeBeanRequest.TasteRequest(
                tasteId = "00000000-0000-4000-8000-000000000004",
                evaluationValue = 5,
            ),
        ),
    ): UpdateCoffeeBeanRequest = UpdateCoffeeBeanRequest(
        shopifyBeanId = shopifyBeanId,
        name = name,
        description = description,
        origin = origin,
        farm = farm,
        roastLevel = roastLevel,
        processingMethod = processingMethod,
        isSpecialty = isSpecialty,
        images = images,
        tastes = tastes,
    )

    @Nested
    inner class 正常系 {
        @Test
        fun `正常にCoffeeBeanを更新できる`() {
            val request = createRequest()
            val bean = usecase.execute(existingBean.id.value, request)!!

            assertEquals("updated-bean-001", bean.shopifyBeanId.value)
            assertEquals("更新後コーヒー豆", bean.name)
            assertEquals("更新後説明文", bean.description)
            assertEquals("ブラジル", bean.origin)
            assertEquals("更新後農園", bean.farm)
            assertEquals(RoastLevel.FRENCH, bean.roastLevel)
            assertEquals(ProcessingMethod.NATURAL, bean.processingMethod)
            assertTrue(bean.isSpecialty)
            assertEquals(1, bean.images.size)
            assertEquals(CoffeeBeanImageType.MAIN, bean.images[0].type)
            assertEquals("https://example.com/updated.png", bean.images[0].imageUrl.value)
            assertEquals(1, bean.tastes.size)
            assertEquals("00000000-0000-4000-8000-000000000004", bean.tastes[0].tasteId.value)
            assertEquals(5, bean.tastes[0].evaluationValue)
        }
    }

    @Nested
    inner class shopId保持 {
        @Test
        fun `更新後もshopIdが保持される`() {
            val bean = usecase.execute(existingBean.id.value, createRequest())!!
            assertEquals(existingBean.shopId, bean.shopId)
        }

        @Test
        fun `更新後もidが保持される`() {
            val bean = usecase.execute(existingBean.id.value, createRequest())!!
            assertEquals(existingBean.id, bean.id)
        }
    }

    @Nested
    inner class 存在しない場合 {
        @Test
        fun `存在しないIDの場合はnullが返る`() {
            val result = usecase.execute("00000000-0000-4000-8000-999999999999", createRequest())
            assertNull(result)
        }
    }

    @Nested
    inner class nullable項目 {
        @Test
        fun `farmがnullでもCoffeeBeanを更新できる`() {
            val bean = usecase.execute(existingBean.id.value, createRequest(farm = null))!!
            assertNull(bean.farm)
        }
    }

    @Nested
    inner class 空コレクション {
        @Test
        fun `画像なしでもCoffeeBeanを更新できる`() {
            val bean = usecase.execute(existingBean.id.value, createRequest(images = emptyList()))!!
            assertEquals(0, bean.images.size)
        }

        @Test
        fun `テイストなしでもCoffeeBeanを更新できる`() {
            val bean = usecase.execute(existingBean.id.value, createRequest(tastes = emptyList()))!!
            assertEquals(0, bean.tastes.size)
        }
    }

    @Nested
    inner class リポジトリ保存 {
        @Test
        fun `更新したCoffeeBeanがリポジトリに保存される`() {
            savedBeans.clear()
            usecase.execute(existingBean.id.value, createRequest())
            assertEquals(1, savedBeans.size)
            assertEquals("更新後コーヒー豆", savedBeans[0].name)
        }

        @Test
        fun `存在しないIDの場合はリポジトリに保存されない`() {
            savedBeans.clear()
            usecase.execute("00000000-0000-4000-8000-999999999999", createRequest())
            assertEquals(0, savedBeans.size)
        }
    }

    @Nested
    inner class バリデーション {
        @Test
        fun `nameが空白の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(existingBean.id.value, createRequest(name = ""))
            }
        }

        @Test
        fun `不正な焙煎度の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(existingBean.id.value, createRequest(roastLevel = "INVALID"))
            }
        }

        @Test
        fun `不正な精製方法の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(existingBean.id.value, createRequest(processingMethod = "INVALID"))
            }
        }

        @Test
        fun `不正な画像種別の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(
                    existingBean.id.value,
                    createRequest(
                        images = listOf(
                            UpdateCoffeeBeanRequest.ImageRequest(
                                type = "INVALID",
                                imageUrl = "https://example.com/bean.png",
                            ),
                        ),
                    ),
                )
            }
        }
    }
}
