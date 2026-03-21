package com.mametosho.admin.presentation.controller

import com.mametosho.admin.application.query.CoffeeBeanQueryService
import com.mametosho.admin.application.query.result.CoffeeBeanListResult
import com.mametosho.admin.application.query.result.PagedResult
import com.mametosho.admin.application.usecase.CreateCoffeeBeanUsecase
import com.mametosho.admin.application.usecase.DeleteCoffeeBeanUsecase
import com.mametosho.admin.application.usecase.GetCoffeeBeanUsecase
import com.mametosho.admin.application.usecase.ListCoffeeBeansUsecase
import com.mametosho.admin.application.usecase.UpdateCoffeeBeanUsecase
import com.mametosho.admin.presentation.dto.request.CreateCoffeeBeanRequest
import com.mametosho.admin.presentation.dto.request.UpdateCoffeeBeanRequest
import com.mametosho.domain.model.coffeebean.CoffeeBean
import com.mametosho.domain.model.coffeebean.CoffeeBeanId
import com.mametosho.domain.model.coffeebean.ProcessingMethod
import com.mametosho.domain.model.coffeebean.RoastLevel
import com.mametosho.domain.model.coffeebean.ShopifyBeanId
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.admin.test.FakeImageStorageService
import org.junit.jupiter.api.Nested
import org.springframework.http.HttpStatus
import org.springframework.web.multipart.MultipartFile
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

    private val fakeRepository = object : com.mametosho.domain.repository.CoffeeBeanRepository {
        override fun save(coffeeBean: CoffeeBean) = Unit
        override fun findById(id: com.mametosho.domain.model.coffeebean.CoffeeBeanId): CoffeeBean? = null
        override fun deleteById(id: com.mametosho.domain.model.coffeebean.CoffeeBeanId) = Unit
    }

    private val fakeImageStorageService = FakeImageStorageService

    private val sampleListResult = PagedResult(
        items = listOf(
            CoffeeBeanListResult(
                id = "00000000-0000-4000-8000-000000000001",
                shopId = "00000000-0000-4000-8000-000000000002",
                shopifyBeanId = "test-bean-001",
                name = "テストコーヒー豆",
                description = "テスト説明文",
                origin = "エチオピア",
                farm = "テスト農園",
                roastLevel = "MEDIUM",
                processingMethod = "WASHED",
                isSpecialty = true,
            ),
        ),
        totalCount = 1L,
        page = 0,
        size = 20,
    )

    private val emptyListResult = PagedResult<CoffeeBeanListResult>(
        items = emptyList(),
        totalCount = 0L,
        page = 0,
        size = 20,
    )

    private val fakeQueryService = object : CoffeeBeanQueryService {
        override fun findList(page: Int, size: Int): PagedResult<CoffeeBeanListResult> = sampleListResult
    }

    private fun createController(
        coffeeBean: CoffeeBean = sampleCoffeeBean,
        getResult: CoffeeBean? = sampleCoffeeBean,
        listResult: PagedResult<CoffeeBeanListResult> = sampleListResult,
        updateResult: CoffeeBean? = sampleCoffeeBean,
        deleteResult: Boolean = true,
    ): CoffeeBeanController {
        val fakeGetUsecase = object : GetCoffeeBeanUsecase(fakeRepository) {
            override fun execute(id: String): CoffeeBean? = getResult
        }
        val fakeListUsecase = object : ListCoffeeBeansUsecase(fakeQueryService) {
            override fun execute(page: Int, size: Int): PagedResult<CoffeeBeanListResult> = listResult
        }
        val fakeCreateUsecase = object : CreateCoffeeBeanUsecase(fakeRepository, fakeImageStorageService) {
            override fun execute(request: CreateCoffeeBeanRequest, imageFiles: List<MultipartFile>, imageTypes: List<String>): CoffeeBean = coffeeBean
        }
        val fakeUpdateUsecase = object : UpdateCoffeeBeanUsecase(fakeRepository, fakeImageStorageService) {
            override fun execute(id: String, request: UpdateCoffeeBeanRequest, imageFiles: List<MultipartFile>, imageTypes: List<String>): CoffeeBean? = updateResult
        }
        val fakeDeleteUsecase = object : DeleteCoffeeBeanUsecase(fakeRepository, fakeImageStorageService) {
            override fun execute(id: String): Boolean = deleteResult
        }
        return CoffeeBeanController(fakeGetUsecase, fakeListUsecase, fakeCreateUsecase, fakeUpdateUsecase, fakeDeleteUsecase)
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
        tastes = emptyList(),
    )

    private fun createUpdateRequest(): UpdateCoffeeBeanRequest = UpdateCoffeeBeanRequest(
        shopifyBeanId = "test-bean-001",
        name = "更新コーヒー豆",
        description = "更新説明文",
        origin = "ブラジル",
        farm = "更新農園",
        roastLevel = "FRENCH",
        processingMethod = "NATURAL",
        isSpecialty = true,
        tastes = emptyList(),
    )

    @Nested
    inner class コーヒー豆一覧取得 {
        @Test
        fun `正常にコーヒー豆一覧を取得すると200が返る`() {
            val controller = createController()
            val response = controller.listCoffeeBeans(0, 20)

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(1, response.body?.items?.size)
            assertEquals(1L, response.body?.totalCount)
            assertEquals(0, response.body?.page)
            assertEquals(20, response.body?.size)
        }

        @Test
        fun `結果が0件の場合も200が返る`() {
            val controller = createController(listResult = emptyListResult)
            val response = controller.listCoffeeBeans(0, 20)

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(0, response.body?.items?.size)
            assertEquals(0L, response.body?.totalCount)
        }
    }

    @Nested
    inner class コーヒー豆詳細取得 {
        @Test
        fun `正常にコーヒー豆詳細を取得すると200が返る`() {
            val controller = createController()
            val response = controller.getCoffeeBean("00000000-0000-4000-8000-000000000001")

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals("00000000-0000-4000-8000-000000000001", response.body?.id)
        }

        @Test
        fun `存在しないコーヒー豆を取得すると404が返る`() {
            val controller = createController(getResult = null)
            val response = controller.getCoffeeBean("00000000-0000-4000-8000-999999999999")

            assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        }
    }

    @Nested
    inner class コーヒー豆登録 {
        @Test
        fun `正常にコーヒー豆を登録すると201が返る`() {
            val controller = createController()
            val response = controller.createCoffeeBean(createRequest(), emptyList(), emptyList())

            assertEquals(HttpStatus.CREATED, response.statusCode)
            assertEquals("00000000-0000-4000-8000-000000000001", response.body?.id)
        }
    }

    @Nested
    inner class コーヒー豆更新 {
        @Test
        fun `正常にコーヒー豆を更新すると200が返る`() {
            val controller = createController()
            val response = controller.updateCoffeeBean("00000000-0000-4000-8000-000000000001", createUpdateRequest(), emptyList(), emptyList())

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals("00000000-0000-4000-8000-000000000001", response.body?.id)
        }

        @Test
        fun `存在しないコーヒー豆を更新すると404が返る`() {
            val controller = createController(updateResult = null)
            val response = controller.updateCoffeeBean("00000000-0000-4000-8000-999999999999", createUpdateRequest(), emptyList(), emptyList())

            assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        }
    }

    @Nested
    inner class コーヒー豆削除 {
        @Test
        fun `正常にコーヒー豆を削除すると204が返る`() {
            val controller = createController()
            val response = controller.deleteCoffeeBean("00000000-0000-4000-8000-000000000001")

            assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        }

        @Test
        fun `存在しないコーヒー豆を削除すると404が返る`() {
            val controller = createController(deleteResult = false)
            val response = controller.deleteCoffeeBean("00000000-0000-4000-8000-999999999999")

            assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        }
    }
}
