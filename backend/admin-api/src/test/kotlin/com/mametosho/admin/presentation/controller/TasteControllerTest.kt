package com.mametosho.admin.presentation.controller

import com.mametosho.admin.application.query.TasteQueryService
import com.mametosho.admin.application.query.result.TasteListResult
import com.mametosho.admin.application.usecase.ListTastesUsecase
import org.junit.jupiter.api.Nested
import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class TasteControllerTest {

    private val sampleTastes = listOf(
        TasteListResult(id = "00000000-0000-4000-8000-000000000041", name = "酸味"),
        TasteListResult(id = "00000000-0000-4000-8000-000000000042", name = "苦味"),
        TasteListResult(id = "00000000-0000-4000-8000-000000000043", name = "甘味"),
        TasteListResult(id = "00000000-0000-4000-8000-000000000044", name = "コク"),
        TasteListResult(id = "00000000-0000-4000-8000-000000000045", name = "香り"),
    )

    private fun createController(tastes: List<TasteListResult> = sampleTastes): TasteController {
        val fakeTasteQueryService = object : TasteQueryService {
            override fun findAll(): List<TasteListResult> = tastes
        }
        val fakeListTastesUsecase = object : ListTastesUsecase(fakeTasteQueryService) {
            override fun execute(): List<TasteListResult> = tastes
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
