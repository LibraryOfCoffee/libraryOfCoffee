package com.mametosho.admin.presentation.controller

import com.mametosho.admin.application.usecase.ListTastesUsecase
import com.mametosho.domain.model.taste.Taste
import com.mametosho.domain.model.taste.TasteId
import com.mametosho.domain.repository.TasteRepository
import org.junit.jupiter.api.Nested
import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class TasteControllerTest {

    private val sampleTastes = listOf(
        Taste(id = TasteId("00000000-0000-4000-8000-000000000041"), name = "酸味"),
        Taste(id = TasteId("00000000-0000-4000-8000-000000000042"), name = "苦味"),
        Taste(id = TasteId("00000000-0000-4000-8000-000000000043"), name = "甘味"),
        Taste(id = TasteId("00000000-0000-4000-8000-000000000044"), name = "コク"),
        Taste(id = TasteId("00000000-0000-4000-8000-000000000045"), name = "香り"),
    )

    private fun createController(tastes: List<Taste> = sampleTastes): TasteController {
        val fakeTasteRepository = object : TasteRepository {
            override fun findAll(): List<Taste> = tastes
        }
        val fakeListTastesUsecase = object : ListTastesUsecase(fakeTasteRepository) {
            override fun execute(): List<Taste> = tastes
        }
        return TasteController(fakeListTastesUsecase)
    }

    @Nested
    inner class テイスト一覧取得 {
        @Test
        fun `正常にテイスト一覧を取得すると200が返る`() {
            val controller = createController()
            val response = controller.listTastes()

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(5, response.body?.size)
            assertEquals("00000000-0000-4000-8000-000000000041", response.body?.first()?.id)
            assertEquals("酸味", response.body?.first()?.name)
        }

        @Test
        fun `テイストが0件の場合も200が返る`() {
            val controller = createController(tastes = emptyList())
            val response = controller.listTastes()

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(0, response.body?.size)
        }
    }
}
