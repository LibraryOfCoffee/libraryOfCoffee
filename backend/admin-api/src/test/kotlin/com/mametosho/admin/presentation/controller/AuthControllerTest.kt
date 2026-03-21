package com.mametosho.admin.presentation.controller

import com.mametosho.admin.application.service.TokenGenerator
import com.mametosho.admin.application.usecase.AuthenticationException
import com.mametosho.admin.application.usecase.LoginUsecase
import com.mametosho.admin.config.JwtProperties
import com.mametosho.admin.presentation.dto.request.LoginRequest
import com.mametosho.admin.presentation.dto.response.LoginResponse
import com.mametosho.domain.model.administrator.Email
import com.mametosho.domain.repository.AdministratorRepository
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthControllerTest {

    private val jwtProperties = JwtProperties(
        secretKey = "test-secret-key-must-be-at-least-32-bytes-long-for-hmac",
        expirationSeconds = 3600,
    )

    private val fakeRepository = object : AdministratorRepository {
        override fun findByEmail(email: Email) = null
    }
    private val fakePasswordEncoder = object : PasswordEncoder {
        override fun encode(rawPassword: CharSequence?) = ""
        override fun matches(rawPassword: CharSequence?, encodedPassword: String?) = false
    }
    private val fakeTokenGenerator = object : TokenGenerator {
        override fun generate(administrator: com.mametosho.domain.model.administrator.Administrator) = ""
    }

    private fun createController(
        loginResult: String = "fake-jwt-token",
        throwException: Boolean = false,
    ): AuthController {
        val fakeLoginUsecase = object : LoginUsecase(fakeRepository, fakePasswordEncoder, fakeTokenGenerator) {
            override fun execute(email: String, password: String): String {
                if (throwException) throw AuthenticationException()
                return loginResult
            }
        }
        return AuthController(fakeLoginUsecase, jwtProperties)
    }

    @Nested
    inner class ログイン {
        @Test
        fun `正常にログインすると200とトークンが返る`() {
            val controller = createController()
            val request = LoginRequest(email = "admin@mametosho.com", password = "password123")

            val response = controller.login(request)

            assertEquals(HttpStatus.OK, response.statusCode)
            val body = response.body as LoginResponse
            assertEquals("fake-jwt-token", body.accessToken)
            assertEquals("Bearer", body.tokenType)
            assertEquals(3600L, body.expiresIn)
        }

        @Test
        fun `認証失敗するとAuthenticationExceptionが発生する`() {
            val controller = createController(throwException = true)
            val request = LoginRequest(email = "unknown@mametosho.com", password = "wrong")

            assertThrows<AuthenticationException> {
                controller.login(request)
            }
        }
    }
}
