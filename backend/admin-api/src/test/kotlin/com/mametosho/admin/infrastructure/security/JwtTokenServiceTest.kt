package com.mametosho.admin.infrastructure.security

import com.mametosho.admin.config.JwtProperties
import com.mametosho.domain.model.administrator.AdminRole
import com.mametosho.domain.model.administrator.Administrator
import com.mametosho.domain.model.administrator.AdministratorId
import com.mametosho.domain.model.administrator.Email
import com.nimbusds.jwt.SignedJWT
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JwtTokenServiceTest {

    private val jwtProperties = JwtProperties(
        secretKey = "test-secret-key-must-be-at-least-32-bytes-long-for-hmac",
        expirationSeconds = 3600,
    )

    private val service = JwtTokenService(jwtProperties)

    private val sampleAdministrator = Administrator(
        id = AdministratorId("00000000-0000-4000-8000-000000000001"),
        email = Email("admin@mametosho.com"),
        hashedPassword = "hashed",
        role = AdminRole.ADMIN,
    )

    @Nested
    inner class トークン生成 {
        @Test
        fun `JWTトークンが正常に生成される`() {
            val token = service.generate(sampleAdministrator)

            assertTrue(token.isNotBlank())
            val parsed = SignedJWT.parse(token)
            val claims = parsed.jwtClaimsSet
            assertEquals("00000000-0000-4000-8000-000000000001", claims.subject)
            assertEquals("admin@mametosho.com", claims.getStringClaim("email"))
            assertEquals("ADMIN", claims.getStringClaim("role"))
        }

        @Test
        fun `トークンの有効期限が設定される`() {
            val token = service.generate(sampleAdministrator)

            val parsed = SignedJWT.parse(token)
            val claims = parsed.jwtClaimsSet
            val diffSeconds = (claims.expirationTime.time - claims.issueTime.time) / 1000
            assertEquals(3600L, diffSeconds)
        }
    }
}
