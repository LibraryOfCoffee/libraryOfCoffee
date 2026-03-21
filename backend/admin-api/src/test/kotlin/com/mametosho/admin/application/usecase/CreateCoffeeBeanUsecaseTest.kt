package com.mametosho.admin.application.usecase

import com.mametosho.admin.presentation.dto.request.CreateCoffeeBeanRequest
import com.mametosho.domain.model.coffeebean.CoffeeBean
import com.mametosho.domain.model.coffeebean.ProcessingMethod
import com.mametosho.domain.model.coffeebean.RoastLevel
import com.mametosho.domain.repository.CoffeeBeanRepository
import com.mametosho.admin.test.FakeImageStorageService
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CreateCoffeeBeanUsecaseTest {

    private val savedBeans = mutableListOf<CoffeeBean>()

    private val fakeRepository = object : CoffeeBeanRepository {
        override fun save(coffeeBean: CoffeeBean) {
            savedBeans.add(coffeeBean)
        }

        override fun findById(id: com.mametosho.domain.model.coffeebean.CoffeeBeanId): CoffeeBean? = null
        override fun deleteById(id: com.mametosho.domain.model.coffeebean.CoffeeBeanId) = Unit
    }

    private val usecase = CreateCoffeeBeanUsecase(fakeRepository, FakeImageStorageService)

    private fun createRequest(
        shopId: String = "00000000-0000-4000-8000-000000000001",
        shopifyBeanId: String = "test-bean-001",
        name: String = "テストコーヒー豆",
        description: String = "テスト説明文",
        origin: String = "エチオピア",
        farm: String? = "テスト農園",
        roastLevel: String = "MEDIUM",
        processingMethod: String = "WASHED",
        isSpecialty: Boolean = true,
        tastes: List<CreateCoffeeBeanRequest.TasteRequest> = listOf(
            CreateCoffeeBeanRequest.TasteRequest(
                tasteId = "00000000-0000-4000-8000-000000000002",
                evaluationValue = 3,
            ),
        ),
    ): CreateCoffeeBeanRequest = CreateCoffeeBeanRequest(
        shopId = shopId,
        shopifyBeanId = shopifyBeanId,
        name = name,
        description = description,
        origin = origin,
        farm = farm,
        roastLevel = roastLevel,
        processingMethod = processingMethod,
        isSpecialty = isSpecialty,
        tastes = tastes,
    )

    @Nested
    inner class 正常系 {
        @Test
        fun `正常にCoffeeBeanを作成できる`() {
            val request = createRequest()
            val bean = usecase.execute(request, emptyList(), emptyList())

            assertEquals("00000000-0000-4000-8000-000000000001", bean.shopId.value)
            assertEquals("test-bean-001", bean.shopifyBeanId.value)
            assertEquals("テストコーヒー豆", bean.name)
            assertEquals("テスト説明文", bean.description)
            assertEquals("エチオピア", bean.origin)
            assertEquals("テスト農園", bean.farm)
            assertEquals(RoastLevel.MEDIUM, bean.roastLevel)
            assertEquals(ProcessingMethod.WASHED, bean.processingMethod)
            assertTrue(bean.isSpecialty)
            assertEquals(1, bean.tastes.size)
            assertEquals("00000000-0000-4000-8000-000000000002", bean.tastes[0].tasteId.value)
            assertEquals(3, bean.tastes[0].evaluationValue)
        }
    }

    @Nested
    inner class UUID自動生成 {
        @Test
        fun `CoffeeBeanIdがUUID形式で自動生成される`() {
            val bean = usecase.execute(createRequest(), emptyList(), emptyList())
            val uuidRegex = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
            assertTrue(uuidRegex.matches(bean.id.value))
        }

        @Test
        fun `CoffeeBeanTasteIdがUUID形式で自動生成される`() {
            val bean = usecase.execute(createRequest(), emptyList(), emptyList())
            val uuidRegex = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
            assertTrue(uuidRegex.matches(bean.tastes[0].id.value))
        }
    }

    @Nested
    inner class nullable項目 {
        @Test
        fun `farmがnullでもCoffeeBeanを作成できる`() {
            val bean = usecase.execute(createRequest(farm = null), emptyList(), emptyList())
            assertNull(bean.farm)
        }
    }

    @Nested
    inner class 空コレクション {
        @Test
        fun `画像なしでもCoffeeBeanを作成できる`() {
            val bean = usecase.execute(createRequest(), emptyList(), emptyList())
            assertEquals(0, bean.images.size)
        }

        @Test
        fun `テイストなしでもCoffeeBeanを作成できる`() {
            val bean = usecase.execute(createRequest(tastes = emptyList()), emptyList(), emptyList())
            assertEquals(0, bean.tastes.size)
        }
    }

    @Nested
    inner class リポジトリ保存 {
        @Test
        fun `作成したCoffeeBeanがリポジトリに保存される`() {
            savedBeans.clear()
            usecase.execute(createRequest(), emptyList(), emptyList())
            assertEquals(1, savedBeans.size)
            assertEquals("テストコーヒー豆", savedBeans[0].name)
        }
    }

    @Nested
    inner class バリデーション {
        @Test
        fun `nameが空白の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(createRequest(name = ""), emptyList(), emptyList())
            }
        }

        @Test
        fun `不正な焙煎度の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(createRequest(roastLevel = "INVALID"), emptyList(), emptyList())
            }
        }

        @Test
        fun `不正な精製方法の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(createRequest(processingMethod = "INVALID"), emptyList(), emptyList())
            }
        }
    }
}
