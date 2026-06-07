package com.mametosho.admin.application.usecase

import com.mametosho.admin.application.service.ImageUpload
import com.mametosho.admin.presentation.dto.request.UpdateCoffeeBeanRequest
import com.mametosho.admin.test.FakeImageStorageService
import com.mametosho.domain.model.coffeebean.CoffeeBean
import com.mametosho.domain.model.coffeebean.CoffeeBeanId
import com.mametosho.domain.model.coffeebean.ProcessingMethod
import com.mametosho.domain.model.coffeebean.RoastLevel
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.repository.CoffeeBeanRepository
import com.mametosho.domain.service.ImageStorageService
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import org.springframework.mock.web.MockMultipartFile
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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
        publishStatus = "PUBLISHED",
        images = listOf("MAIN" to "https://example.com/bean.jpg"),
        tastes = listOf("00000000-0000-4000-8000-000000000041" to 3),
    )

    private val existingImageId = existingBean.images[0].id.value

    private val fakeRepository = object : CoffeeBeanRepository {
        override fun save(coffeeBean: CoffeeBean) {
            savedBeans.add(coffeeBean)
        }

        override fun findById(id: CoffeeBeanId): CoffeeBean? {
            return if (id == existingBean.id) existingBean else null
        }

        override fun deleteById(id: CoffeeBeanId) = Unit
        override fun invalidateByShopId(shopId: ShopId) = Unit
    }

    private val usecase = UpdateCoffeeBeanUsecase(fakeRepository, FakeImageStorageService)

    private val imageFile = MockMultipartFile("images", "bean.jpg", "image/jpeg", byteArrayOf(1))

    private fun createRequest(
        shopId: String = "00000000-0000-4000-8000-000000000002",
        shopifyBeanId: String = "updated-bean-001",
        name: String = "更新後コーヒー豆",
        description: String = "更新後説明文",
        origin: String = "ブラジル",
        farm: String? = "更新後農園",
        roastLevel: String = "FRENCH",
        processingMethod: String = "NATURAL",
        isSpecialty: Boolean = true,
        publishStatus: String = "PUBLISHED",
        tastes: List<UpdateCoffeeBeanRequest.TasteRequest> = listOf(
            UpdateCoffeeBeanRequest.TasteRequest(
                tasteId = "00000000-0000-4000-8000-000000000042",
                evaluationValue = 5,
            ),
        ),
    ): UpdateCoffeeBeanRequest = UpdateCoffeeBeanRequest(
        shopId = shopId,
        shopifyBeanId = shopifyBeanId,
        name = name,
        description = description,
        origin = origin,
        farm = farm,
        roastLevel = roastLevel,
        processingMethod = processingMethod,
        isSpecialty = isSpecialty,
        publishStatus = publishStatus,
        tastes = tastes,
    )

    @Nested
    inner class 正常系 {
        @Test
        fun `正常にCoffeeBeanを更新できる`() {
            val request = createRequest()
            val bean = usecase.execute(existingBean.id.value, request, listOf(ImageUpload("MAIN", imageFile)), emptyList())!!

            assertEquals("updated-bean-001", bean.shopifyBeanId.value)
            assertEquals("更新後コーヒー豆", bean.name)
            assertEquals("更新後説明文", bean.description)
            assertEquals("ブラジル", bean.origin)
            assertEquals("更新後農園", bean.farm)
            assertEquals(RoastLevel.FRENCH, bean.roastLevel)
            assertEquals(ProcessingMethod.NATURAL, bean.processingMethod)
            assertTrue(bean.isSpecialty)
            assertEquals(1, bean.tastes.size)
            assertEquals("00000000-0000-4000-8000-000000000042", bean.tastes[0].tasteId.value)
            assertEquals(5, bean.tastes[0].evaluationValue)
        }
    }

    @Nested
    inner class shopId更新 {
        @Test
        fun `shopIdが更新される`() {
            val bean = usecase.execute(
                existingBean.id.value,
                createRequest(shopId = "00000000-0000-4000-8000-000000000099"),
                listOf(ImageUpload("MAIN", imageFile)),
                emptyList(),
            )!!
            assertEquals("00000000-0000-4000-8000-000000000099", bean.shopId.value)
        }

        @Test
        fun `更新後もidが保持される`() {
            val bean = usecase.execute(existingBean.id.value, createRequest(), listOf(ImageUpload("MAIN", imageFile)), emptyList())!!
            assertEquals(existingBean.id, bean.id)
        }
    }

    @Nested
    inner class 存在しない場合 {
        @Test
        fun `存在しないIDの場合はnullが返る`() {
            val result = usecase.execute(
                "00000000-0000-4000-8000-999999999999",
                createRequest(),
                listOf(ImageUpload("MAIN", imageFile)),
                emptyList(),
            )
            assertNull(result)
        }
    }

    @Nested
    inner class nullable項目 {
        @Test
        fun `farmがnullでもCoffeeBeanを更新できる`() {
            val bean = usecase.execute(
                existingBean.id.value,
                createRequest(farm = null),
                listOf(ImageUpload("MAIN", imageFile)),
                emptyList(),
            )!!
            assertNull(bean.farm)
        }
    }

    @Nested
    inner class 空コレクション {
        @Test
        fun `keepImageIdsで既存画像を保持できる`() {
            val bean = usecase.execute(existingBean.id.value, createRequest(), emptyList(), listOf(existingImageId))!!
            assertEquals(1, bean.images.size)
        }

        @Test
        fun `keepImageIdsが空かつ新規画像なしの場合は画像が全削除される`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(existingBean.id.value, createRequest(), emptyList(), emptyList())
            }
        }

        @Test
        fun `テイストなしではCoffeeBeanを更新できない`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(
                    existingBean.id.value,
                    createRequest(tastes = emptyList()),
                    listOf(ImageUpload("MAIN", imageFile)),
                    emptyList(),
                )
            }
        }
    }

    @Nested
    inner class 画像置換 {
        @Test
        fun `新規画像をアップロードすると旧画像が置換される`() {
            val bean = usecase.execute(existingBean.id.value, createRequest(), listOf(ImageUpload("MAIN", imageFile)), emptyList())!!
            assertEquals(1, bean.images.size)
            assertTrue(bean.images[0].image.url.endsWith(".jpg"))
        }

        @Test
        fun `keepImageIdsに含まれる画像は保持され新規画像が追加される`() {
            val secondBean = CoffeeBean.create(
                shopId = "00000000-0000-4000-8000-000000000002",
                shopifyBeanId = "multi-image-bean",
                name = "複数画像豆",
                description = "説明",
                origin = "コロンビア",
                farm = null,
                roastLevel = "MEDIUM",
                processingMethod = "HONEY",
                isSpecialty = false,
                publishStatus = "DRAFT",
                images = listOf("MAIN" to "https://example.com/old-main.jpg"),
                tastes = listOf("00000000-0000-4000-8000-000000000041" to 3),
            )
            val repo = object : CoffeeBeanRepository {
                override fun save(coffeeBean: CoffeeBean) = Unit
                override fun findById(id: CoffeeBeanId): CoffeeBean? = if (id == secondBean.id) secondBean else null
                override fun deleteById(id: CoffeeBeanId) = Unit
                override fun invalidateByShopId(shopId: ShopId) = Unit
            }
            val uc = UpdateCoffeeBeanUsecase(repo, FakeImageStorageService)
            val newFile = MockMultipartFile("images", "new-main.jpg", "image/jpeg", byteArrayOf(2))
            val bean = uc.execute(secondBean.id.value, createRequest(), listOf(ImageUpload("MAIN", newFile)), emptyList())!!
            assertEquals(1, bean.images.size)
        }
    }

    @Nested
    inner class リポジトリ保存 {
        @Test
        fun `更新したCoffeeBeanがリポジトリに保存される`() {
            savedBeans.clear()
            usecase.execute(existingBean.id.value, createRequest(), listOf(ImageUpload("MAIN", imageFile)), emptyList())
            assertEquals(1, savedBeans.size)
            assertEquals("更新後コーヒー豆", savedBeans[0].name)
        }

        @Test
        fun `存在しないIDの場合はリポジトリに保存されない`() {
            savedBeans.clear()
            usecase.execute("00000000-0000-4000-8000-999999999999", createRequest(), listOf(ImageUpload("MAIN", imageFile)), emptyList())
            assertEquals(0, savedBeans.size)
        }
    }

    @Nested
    inner class バリデーション {
        @Test
        fun `nameが空白の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(existingBean.id.value, createRequest(name = ""), listOf(ImageUpload("MAIN", imageFile)), emptyList())
            }
        }

        @Test
        fun `不正な焙煎度の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(
                    existingBean.id.value,
                    createRequest(roastLevel = "INVALID"),
                    listOf(ImageUpload("MAIN", imageFile)),
                    emptyList(),
                )
            }
        }

        @Test
        fun `不正な精製方法の場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(
                    existingBean.id.value,
                    createRequest(processingMethod = "INVALID"),
                    listOf(ImageUpload("MAIN", imageFile)),
                    emptyList(),
                )
            }
        }

        @Test
        fun `不正なテイストIDの場合は例外が発生する`() {
            assertThrows<IllegalArgumentException> {
                usecase.execute(
                    existingBean.id.value,
                    createRequest(
                        tastes = listOf(
                            UpdateCoffeeBeanRequest.TasteRequest(
                                tasteId = "invalid-uuid",
                                evaluationValue = 3,
                            ),
                        ),
                    ),
                    listOf(ImageUpload("MAIN", imageFile)),
                    emptyList(),
                )
            }
        }
    }

    @Nested
    inner class ファイルサイズバリデーション {
        @Test
        fun `1MBを超える画像は例外が発生する`() {
            val largeFile = MockMultipartFile("images", "large.jpg", "image/jpeg", ByteArray(1024 * 1024 + 1))
            assertThrows<IllegalArgumentException> {
                usecase.execute(existingBean.id.value, createRequest(), listOf(ImageUpload("MAIN", largeFile)), emptyList())
            }
        }
    }

    @Nested
    inner class S3削除 {
        private fun trackingUsecase(): Pair<UpdateCoffeeBeanUsecase, MutableList<String>> {
            val deletedKeys = mutableListOf<String>()
            val service = object : ImageStorageService by FakeImageStorageService {
                override fun delete(key: String) { deletedKeys.add(key) }
            }
            return UpdateCoffeeBeanUsecase(fakeRepository, service) to deletedKeys
        }

        @Test
        fun `keepImageIdsに含まれない画像のS3キーが削除される`() {
            val (uc, deletedKeys) = trackingUsecase()
            uc.execute(existingBean.id.value, createRequest(), listOf(ImageUpload("MAIN", imageFile)), emptyList())

            val oldUrl = existingBean.images[0].image.url
            val expectedKey = FakeImageStorageService.extractKey(oldUrl)!!
            assertTrue(deletedKeys.contains(expectedKey), "旧画像のS3キーが削除されていない: $expectedKey")
        }

        @Test
        fun `keepImageIdsに含まれる画像はS3から削除されない`() {
            val (uc, deletedKeys) = trackingUsecase()
            uc.execute(existingBean.id.value, createRequest(), emptyList(), listOf(existingImageId))

            assertTrue(deletedKeys.isEmpty(), "保持対象の画像が削除されてはならない: $deletedKeys")
        }
    }
}
