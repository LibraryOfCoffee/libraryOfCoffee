package com.mametosho.admin.application.usecase

import com.mametosho.admin.application.service.TokenGenerator
import com.mametosho.domain.model.administrator.AdminRole
import com.mametosho.domain.model.administrator.Administrator
import com.mametosho.domain.model.administrator.AdministratorId
import com.mametosho.domain.model.administrator.Email
import com.mametosho.domain.repository.AdministratorRepository
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import kotlin.test.Test
import kotlin.test.assertEquals

class LoginUsecaseTest {

    private val passwordEncoder = BCryptPasswordEncoder()
    private val hashedPassword = checkNotNull(passwordEncoder.encode("password123")) { "BCrypt encoding failed" }

    private val sampleAdministrator = Administrator(
        id = AdministratorId("00000000-0000-4000-8000-000000000001"),
        email = Email("admin@mametosho.com"),
        hashedPassword = hashedPassword,
        role = AdminRole.ADMIN,
    )

    private val fakeTokenGenerator = object : TokenGenerator {
        override fun generate(administrator: Administrator): String = "fake-jwt-token"
    }

    private fun createUsecase(
        findByEmailResult: Administrator? = sampleAdministrator,
    ): LoginUsecase {
        val fakeRepository = object : AdministratorRepository {
            override fun findByEmail(email: Email): Administrator? = findByEmailResult
        }
        return LoginUsecase(fakeRepository, passwordEncoder, fakeTokenGenerator)
    }

    @Nested
    inner class 正常系 {
        @Test
        fun `正しいメールアドレスとパスワードでトークンが返る`() {
            val usecase = createUsecase()
            val result = usecase.execute("admin@mametosho.com", "password123")

            assertEquals("fake-jwt-token", result)
        }
    }

    @Nested
    inner class 認証失敗 {
        @Test
        fun `存在しないメールアドレスで例外が発生する`() {
            val usecase = createUsecase(findByEmailResult = null)

            assertThrows<AuthenticationException> {
                usecase.execute("unknown@mametosho.com", "password123")
            }
        }

        @Test
        fun `パスワードが一致しない場合例外が発生する`() {
            val usecase = createUsecase()

            assertThrows<AuthenticationException> {
                usecase.execute("admin@mametosho.com", "wrong-password")
            }
        }
    }
}
