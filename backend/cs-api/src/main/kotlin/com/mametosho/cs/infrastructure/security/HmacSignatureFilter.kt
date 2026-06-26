package com.mametosho.cs.infrastructure.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.mametosho.cs.config.HmacProperties
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.StreamUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.security.MessageDigest
import java.time.Instant
import java.time.OffsetDateTime
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.math.abs

/**
 * 全リクエストにHMAC-SHA256署名を要求する認証フィルタ。
 *
 * 署名対象文字列(string-to-sign)は改行区切りで以下を連結したもの:
 * ```
 * {HTTPメソッド}
 * {リクエストURI(パス+クエリ)}
 * {X-Timestamp}
 * {SHA256_HEX(ボディ)}
 * ```
 *
 * クライアントは X-Client-Id / X-Timestamp / X-Signature ヘッダを付与する。
 * openapiプロファイル(swagger自動生成)では無効化される。
 */
@Component
@Profile("!openapi")
class HmacSignatureFilter(
    private val resolver: ApiClientSecretResolver,
    private val properties: HmacProperties,
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(HmacSignatureFilter::class.java)

    // フィルタはMVC層の外で動作するため、Spring管理のメッセージコンバータに依存せず
    // 自前のObjectMapperでErrorResponseをJSON化する(OffsetDateTimeはISO-8601文字列で出力)。
    private val objectMapper = ObjectMapper()
        .findAndRegisterModules()
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        return EXCLUDED_PREFIXES.any { path.startsWith(it) }
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val body = StreamUtils.copyToByteArray(request.inputStream)

        val clientId = request.getHeader(HEADER_CLIENT_ID)
        val timestamp = request.getHeader(HEADER_TIMESTAMP)
        val signature = request.getHeader(HEADER_SIGNATURE)
        if (clientId.isNullOrBlank() || timestamp.isNullOrBlank() || signature.isNullOrBlank()) {
            writeUnauthorized(request, response, "署名ヘッダが不足しています")
            return
        }

        val timestampSeconds = timestamp.toLongOrNull()
        if (timestampSeconds == null || abs(Instant.now().epochSecond - timestampSeconds) > properties.allowedSkewSeconds) {
            writeUnauthorized(request, response, "タイムスタンプが無効です")
            return
        }

        val secret = resolver.resolveSecret(clientId)
        if (secret == null) {
            writeUnauthorized(request, response, "クライアントが認証できません")
            return
        }

        val stringToSign = buildStringToSign(request, timestamp, body)
        val expected = hmacSha256Hex(secret, stringToSign)
        if (!MessageDigest.isEqual(expected.toByteArray(), signature.toByteArray())) {
            writeUnauthorized(request, response, "署名が一致しません")
            return
        }

        filterChain.doFilter(CachedBodyHttpServletRequest(request, body), response)
    }

    private fun buildStringToSign(request: HttpServletRequest, timestamp: String, body: ByteArray): String {
        val uri = request.requestURI + (request.queryString?.let { "?$it" } ?: "")
        return listOf(
            request.method,
            uri,
            timestamp,
            sha256Hex(body),
        ).joinToString("\n")
    }

    private fun sha256Hex(data: ByteArray): String =
        MessageDigest.getInstance("SHA-256").digest(data).toHex()

    private fun hmacSha256Hex(key: ByteArray, data: String): String {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(key, "HmacSHA256"))
        return mac.doFinal(data.toByteArray()).toHex()
    }

    private fun ByteArray.toHex(): String =
        joinToString("") { "%02x".format(it) }

    private fun writeUnauthorized(request: HttpServletRequest, response: HttpServletResponse, reason: String) {
        log.warn("HMAC認証失敗 {} {}: {}", request.method, request.requestURI, reason)
        val status = HttpStatus.UNAUTHORIZED
        response.status = status.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = Charsets.UTF_8.name()
        // presentation層のErrorResponse DTOには依存せず、同一フォーマットのJSONをフィルタ内で構築する。
        val errorBody = linkedMapOf(
            "timestamp" to OffsetDateTime.now(),
            "status" to status.value(),
            "error" to status.reasonPhrase,
            "path" to request.requestURI,
        )
        response.writer.write(objectMapper.writeValueAsString(errorBody))
    }

    companion object {
        private const val HEADER_CLIENT_ID = "X-Client-Id"
        private const val HEADER_TIMESTAMP = "X-Timestamp"
        private const val HEADER_SIGNATURE = "X-Signature"
        private val EXCLUDED_PREFIXES = listOf("/actuator", "/swagger-ui", "/v3/api-docs")
    }
}
