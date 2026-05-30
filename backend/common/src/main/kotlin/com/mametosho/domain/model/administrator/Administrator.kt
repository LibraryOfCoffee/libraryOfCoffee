package com.mametosho.domain.model.administrator

/**
 * 管理者を表す集約ルート。
 *
 * 管理画面にログインする管理者。メールアドレスとパスワードで認証し、ロールに応じた権限で操作を行う。
 */
data class Administrator(
    val id: AdministratorId,
    val email: Email,
    val hashedPassword: String,
    val role: AdminRole,
)
