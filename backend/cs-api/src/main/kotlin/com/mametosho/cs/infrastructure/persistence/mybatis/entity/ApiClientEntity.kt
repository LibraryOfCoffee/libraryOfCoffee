package com.mametosho.cs.infrastructure.persistence.mybatis.entity

/**
 * api_clients テーブルの1行。
 *
 * @property encryptedSecret AES-GCMで封筒暗号化したHMAC秘密鍵(iv||ciphertext||tag)
 */
class ApiClientEntity(
    val id: String,
    val clientId: String,
    val description: String,
    val encryptedSecret: ByteArray,
    val isActive: Boolean,
)
