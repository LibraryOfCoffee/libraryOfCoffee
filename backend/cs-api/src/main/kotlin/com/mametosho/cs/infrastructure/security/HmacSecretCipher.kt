package com.mametosho.cs.infrastructure.security

import com.mametosho.cs.config.HmacProperties
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * クライアント秘密鍵をマスター鍵で封筒暗号化(AES-GCM)するユーティリティ。
 *
 * 暗号化結果のフォーマットは `iv(12byte) || ciphertext || tag(16byte)` の連結バイト列。
 * DB漏洩時もマスター鍵が無ければ秘密鍵を復元できないようにするのが目的。
 *
 * openapiプロファイル(swagger自動生成)ではマスター鍵を持たないため無効化する。
 */
@Component
@Profile("!openapi")
class HmacSecretCipher(properties: HmacProperties) {

    private val secretKey = SecretKeySpec(hexToBytes(properties.masterKey), "AES")

    fun encrypt(plaintext: ByteArray): ByteArray {
        val iv = ByteArray(IV_LENGTH).also { SecureRandom().nextBytes(it) }
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, GCMParameterSpec(TAG_LENGTH_BITS, iv))
        return iv + cipher.doFinal(plaintext)
    }

    fun decrypt(blob: ByteArray): ByteArray {
        require(blob.size > IV_LENGTH) { "暗号文が不正です" }
        val iv = blob.copyOfRange(0, IV_LENGTH)
        val cipherText = blob.copyOfRange(IV_LENGTH, blob.size)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(TAG_LENGTH_BITS, iv))
        return cipher.doFinal(cipherText)
    }

    private fun hexToBytes(hex: String): ByteArray {
        require(hex.length % 2 == 0) { "マスター鍵のhex長が不正です" }
        return ByteArray(hex.length / 2) { i ->
            hex.substring(i * 2, i * 2 + 2).toInt(HEX_RADIX).toByte()
        }
    }

    companion object {
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val IV_LENGTH = 12
        private const val TAG_LENGTH_BITS = 128
        private const val HEX_RADIX = 16
    }
}
