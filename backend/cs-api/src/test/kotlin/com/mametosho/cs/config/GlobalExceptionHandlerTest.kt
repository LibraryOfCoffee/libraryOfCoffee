package com.mametosho.cs.config

import org.junit.jupiter.api.Nested
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.servlet.resource.NoResourceFoundException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class GlobalExceptionHandlerTest {

    private val handler = GlobalExceptionHandler()

    private fun request(path: String = "/api/shops/invalid-id"): MockHttpServletRequest =
        MockHttpServletRequest("GET", path)

    @Nested
    inner class IllegalArgumentExceptionの処理 {
        @Test
        fun `400 BadRequestを返す`() {
            val response = handler.handleIllegalArgument(
                IllegalArgumentException("ShopId must be a valid UUID format"),
                request(),
            )

            assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        }

        @Test
        fun `レスポンスボディにステータス・エラー概要・パスが設定される`() {
            val response = handler.handleIllegalArgument(
                IllegalArgumentException("ShopId must be a valid UUID format"),
                request(path = "/api/shops/invalid-id"),
            )

            val body = response.body
            assertNotNull(body)
            assertEquals(400, body.status)
            assertEquals("Bad Request", body.error)
            assertEquals("/api/shops/invalid-id", body.path)
            assertNotNull(body.timestamp)
        }
    }

    @Nested
    inner class NoResourceFoundExceptionの処理 {
        @Test
        fun `404 NotFoundを返す`() {
            val response = handler.handleNoResourceFound(
                NoResourceFoundException(
                    HttpMethod.GET,
                    "/api/shops/00000000-0000-4000-8000-000000000999",
                    "shops/00000000-0000-4000-8000-000000000999",
                ),
                request(path = "/api/shops/00000000-0000-4000-8000-000000000999"),
            )

            assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
            assertEquals(404, response.body?.status)
            assertEquals("Not Found", response.body?.error)
        }
    }

    @Nested
    inner class その他例外の処理 {
        @Test
        fun `500 InternalServerErrorを返す`() {
            val response = handler.handleException(
                RuntimeException("unexpected"),
                request(path = "/api/shops"),
            )

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
            assertEquals(500, response.body?.status)
            assertEquals("Internal Server Error", response.body?.error)
        }
    }
}
