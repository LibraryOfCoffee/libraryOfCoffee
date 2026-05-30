package com.mametosho.admin.application.usecase

import com.mametosho.domain.model.coffeebean.CoffeeBean
import com.mametosho.domain.model.coffeebean.CoffeeBeanId
import com.mametosho.domain.repository.CoffeeBeanRepository
import com.mametosho.admin.test.FakeImageStorageService
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DeleteCoffeeBeanUsecaseTest {

    private val deletedIds = mutableListOf<CoffeeBeanId>()

    private val existingBean = CoffeeBean.create(
        shopId = "00000000-0000-4000-8000-000000000002",
        shopifyBeanId = "test-bean-001",
        name = "テストコーヒー豆",
        description = "テスト説明文",
        origin = "エチオピア",
        farm = "テスト農園",
        roastLevel = "MEDIUM",
        processingMethod = "WASHED",
        isSpecialty = true,
        publishStatus = "PUBLISHED",
        images = listOf("MAIN" to "https://example.com/bean.jpg"),
        tastes = listOf("00000000-0000-4000-8000-000000000041" to 3),
    )

    private val fakeRepository = object : CoffeeBeanRepository {
        override fun save(coffeeBean: CoffeeBean) = Unit

        override fun findById(id: CoffeeBeanId): CoffeeBean? {
            return if (id == existingBean.id) existingBean else null
        }

        override fun deleteById(id: CoffeeBeanId) {
            deletedIds.add(id)
        }
    }

    private val usecase = DeleteCoffeeBeanUsecase(fakeRepository, FakeImageStorageService)

    @Nested
    inner class 正常系 {
        @Test
        fun `存在するCoffeeBeanを削除するとtrueが返る`() {
            val result = usecase.execute(existingBean.id.value)
            assertTrue(result)
        }
    }

    @Nested
    inner class 存在しない場合 {
        @Test
        fun `存在しないIDの場合はfalseが返る`() {
            val result = usecase.execute("00000000-0000-4000-8000-999999999999")
            assertFalse(result)
        }
    }

    @Nested
    inner class リポジトリ削除 {
        @Test
        fun `削除時にリポジトリのdeleteByIdが呼ばれる`() {
            deletedIds.clear()
            usecase.execute(existingBean.id.value)
            assertEquals(1, deletedIds.size)
            assertEquals(existingBean.id, deletedIds[0])
        }

        @Test
        fun `存在しないIDの場合はリポジトリのdeleteByIdが呼ばれない`() {
            deletedIds.clear()
            usecase.execute("00000000-0000-4000-8000-999999999999")
            assertEquals(0, deletedIds.size)
        }
    }
}
