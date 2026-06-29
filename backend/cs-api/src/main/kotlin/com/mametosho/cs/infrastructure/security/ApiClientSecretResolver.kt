package com.mametosho.cs.infrastructure.security

import com.mametosho.cs.infrastructure.persistence.mybatis.mapper.ApiClientMapper
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

/**
 * X-Client-Id から有効なクライアントのHMAC秘密鍵(復号済み)を解決する。
 *
 * 未知のclientId、もしくは is_active=false の場合は null を返す。
 */
@Component
@Profile("!openapi")
class ApiClientSecretResolver(
    private val apiClientMapper: ApiClientMapper,
    private val cipher: HmacSecretCipher,
) {
    fun resolveSecret(clientId: String): ByteArray? {
        val entity = apiClientMapper.findActiveByClientId(clientId) ?: return null
        return cipher.decrypt(entity.encryptedSecret)
    }
}
