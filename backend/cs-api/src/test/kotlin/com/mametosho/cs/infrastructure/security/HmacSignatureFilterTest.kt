package com.mametosho.cs.infrastructure.security

import com.mametosho.cs.config.HmacProperties
import com.mametosho.cs.infrastructure.persistence.mybatis.entity.ApiClientEntity
import com.mametosho.cs.infrastructure.persistence.mybatis.mapper.ApiClientMapper
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import java.security.MessageDigest
import java.time.Instant
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class HmacSignatureFilterTest {

    private val masterKey = "7f079f53a156f76466e1443ce4f3a196fcea51b275a55b7052e20f727e98faf6"
    private val knownClientId = "ssr-frontend"
    private val clientSecret = "client-shared-secret".toByteArray()

    private val cipher = HmacSecretCipher(HmacProperties(masterKey = masterKey))

    // ApiClientMapper を匿名objectでスタブ実装(モックライブラリ未使用)
    private val stubMapper = object : ApiClientMapper {
        override fun findActiveByClientId(clientId: String): ApiClientEntity? =
            if (clientId == knownClientId) {
                ApiClientEntity(
                    id = "00000000-0000-4000-8000-000000000001",
                    clientId = knownClientId,
                    description = "テスト用クライアント",
                    encryptedSecret = cipher.encrypt(clientSecret),
                    isActive = true,
                )
            } else {
                null
            }
    }

    private val resolver = ApiClientSecretResolver(stubMapper, cipher)
    private val filter = HmacSignatureFilter(resolver, HmacProperties(masterKey = masterKey))

    private fun buildRequest(
        method: String = "GET",
        uri: String = "/api/shops",
        query: String? = "page=0&size=100",
        body: ByteArray = ByteArray(0),
    ): MockHttpServletRequest {
        val request = MockHttpServletRequest(method, uri)
        request.requestURI = uri
        request.queryString = query
        request.setContent(body)
        return request
    }

    private fun sha256Hex(data: ByteArray): String =
        MessageDigest.getInstance("SHA-256").digest(data).joinToString("") { "%02x".format(it) }

    private fun sign(
        secret: ByteArray,
        method: String,
        pathWithQuery: String,
        timestamp: String,
        body: ByteArray,
    ): String {
        val stringToSign = listOf(method, pathWithQuery, timestamp, sha256Hex(body)).joinToString("\n")
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(secret, "HmacSHA256"))
        return mac.doFinal(stringToSign.toByteArray()).joinToString("") { "%02x".format(it) }
    }

    private fun MockHttpServletRequest.applySignatureHeaders(
        clientId: String,
        timestamp: String,
        signature: String,
    ) {
        addHeader("X-Client-Id", clientId)
        addHeader("X-Timestamp", timestamp)
        addHeader("X-Signature", signature)
    }

    @Test
    fun `正しい署名ならchainを通過する`() {
        val timestamp = Instant.now().epochSecond.toString()
        val request = buildRequest()
        val signature = sign(clientSecret, "GET", "/api/shops?page=0&size=100", timestamp, ByteArray(0))
        request.applySignatureHeaders(knownClientId, timestamp, signature)

        val response = MockHttpServletResponse()
        val chain = MockFilterChain()

        filter.doFilter(request, response, chain)

        assertNotNull(chain.request, "署名が正しい場合はchainに委譲されるべき")
        assertEquals(HttpStatus.OK.value(), response.status)
    }

    @Test
    fun `署名ヘッダが欠落していたら401`() {
        val request = buildRequest()
        val response = MockHttpServletResponse()
        val chain = MockFilterChain()

        filter.doFilter(request, response, chain)

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.status)
        assertNull(chain.request, "認証失敗時はchainに委譲しない")
    }

    @Test
    fun `署名が一致しなければ401`() {
        val timestamp = Instant.now().epochSecond.toString()
        val request = buildRequest()
        request.applySignatureHeaders(knownClientId, timestamp, "deadbeef")

        val response = MockHttpServletResponse()
        val chain = MockFilterChain()

        filter.doFilter(request, response, chain)

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.status)
        assertNull(chain.request)
    }

    @Test
    fun `タイムスタンプが期限切れなら401`() {
        val expiredTimestamp = (Instant.now().epochSecond - 10_000).toString()
        val request = buildRequest()
        val signature = sign(clientSecret, "GET", "/api/shops?page=0&size=100", expiredTimestamp, ByteArray(0))
        request.applySignatureHeaders(knownClientId, expiredTimestamp, signature)

        val response = MockHttpServletResponse()
        val chain = MockFilterChain()

        filter.doFilter(request, response, chain)

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.status)
        assertNull(chain.request)
    }

    @Test
    fun `未知のclientIdなら401`() {
        val timestamp = Instant.now().epochSecond.toString()
        val request = buildRequest()
        val signature = sign(clientSecret, "GET", "/api/shops?page=0&size=100", timestamp, ByteArray(0))
        request.applySignatureHeaders("unknown-client", timestamp, signature)

        val response = MockHttpServletResponse()
        val chain = MockFilterChain()

        filter.doFilter(request, response, chain)

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.status)
        assertNull(chain.request)
    }

    @Test
    fun `除外パスは無署名でも通過する`() {
        val request = buildRequest(uri = "/actuator/health", query = null)
        val response = MockHttpServletResponse()
        val chain = MockFilterChain()

        filter.doFilter(request, response, chain)

        assertNotNull(chain.request, "除外パスは署名検証をスキップしてchainに委譲されるべき")
        assertEquals(HttpStatus.OK.value(), response.status)
    }
}
