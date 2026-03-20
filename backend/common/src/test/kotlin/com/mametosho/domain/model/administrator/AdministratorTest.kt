package com.mametosho.domain.model.administrator

import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals

class AdministratorTest {

    @Nested
    inner class 生成テスト {

        @Test
        fun `正常にAdministratorを生成できる`() {
            val admin = Administrator(
                id = AdministratorId("00000000-0000-4000-8000-000000000012"),
                email = Email("admin@example.com"),
                hashedPassword = "hashed-password",
                role = AdminRole.ADMIN,
            )
            assertEquals("admin@example.com", admin.email.value)
            assertEquals(AdminRole.ADMIN, admin.role)
        }

        @Test
        fun `STAFFロールで生成できる`() {
            val admin = Administrator(
                id = AdministratorId("00000000-0000-4000-8000-000000000012"),
                email = Email("staff@example.com"),
                hashedPassword = "hashed-password",
                role = AdminRole.STAFF,
            )
            assertEquals(AdminRole.STAFF, admin.role)
        }
    }
}
