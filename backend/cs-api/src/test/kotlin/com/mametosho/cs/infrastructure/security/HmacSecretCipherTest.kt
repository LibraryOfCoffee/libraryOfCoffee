package com.mametosho.cs.infrastructure.security

import com.mametosho.cs.config.HmacProperties
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HmacSecretCipherTest {

    private val cipher = HmacSecretCipher(
        HmacProperties(masterKey = "7f079f53a156f76466e1443ce4f3a196fcea51b275a55b7052e20f727e98faf6"),
    )

    @Test
    fun `encryptしたものをdecryptすると元の平文に戻る`() {
        val plaintext = "4cc1009cc9bfeb01bcce0c7a8418d9ea3fe7cf9e3e30bff3".toByteArray()

        val encrypted = cipher.encrypt(plaintext)
        val decrypted = cipher.decrypt(encrypted)

        assertContentEquals(plaintext, decrypted)
    }

    @Test
    fun `同じ平文を2回encryptしても暗号文は異なる(ランダムIV)`() {
        val plaintext = "secret-value".toByteArray()

        val first = cipher.encrypt(plaintext)
        val second = cipher.encrypt(plaintext)

        assertFalse(first.contentEquals(second), "ランダムIVにより暗号文は毎回異なるべき")
        // どちらも復号すれば同じ平文に戻る
        assertContentEquals(plaintext, cipher.decrypt(first))
        assertContentEquals(plaintext, cipher.decrypt(second))
    }

    @Test
    fun `改竄された暗号文のdecryptは例外になる`() {
        val plaintext = "tamper-me".toByteArray()
        val encrypted = cipher.encrypt(plaintext)

        // 末尾(GCM認証タグ領域)を1バイト反転させる
        val tampered = encrypted.copyOf()
        tampered[tampered.lastIndex] = (tampered.last().toInt() xor 0xFF).toByte()

        assertThrows<Exception> {
            cipher.decrypt(tampered)
        }
    }

    @Test
    fun `短すぎる不正なblobのdecryptは例外になる`() {
        assertThrows<IllegalArgumentException> {
            cipher.decrypt(ByteArray(4))
        }
    }

    @Test
    fun `IV長未満のblobは不正として弾く`() {
        val tooShort = ByteArray(8)
        val thrown = runCatching { cipher.decrypt(tooShort) }.isFailure
        assertTrue(thrown)
    }
}
