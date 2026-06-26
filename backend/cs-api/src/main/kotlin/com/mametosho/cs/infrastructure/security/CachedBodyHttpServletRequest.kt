package com.mametosho.cs.infrastructure.security

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader

/**
 * リクエストボディをバッファリングし、署名検証(ハッシュ計算)後に下流の
 * コントローラが再度ボディを読めるようにするラッパー。
 *
 * 現状のCS APIはGET中心でボディは空だが、将来のPOST/Shopify連携に備えて汎用化している。
 */
class CachedBodyHttpServletRequest(
    request: HttpServletRequest,
    private val cachedBody: ByteArray,
) : HttpServletRequestWrapper(request) {

    override fun getInputStream(): ServletInputStream {
        val byteStream = ByteArrayInputStream(cachedBody)
        return object : ServletInputStream() {
            override fun read(): Int = byteStream.read()
            override fun isFinished(): Boolean = byteStream.available() == 0
            override fun isReady(): Boolean = true
            override fun setReadListener(listener: ReadListener?) = Unit
        }
    }

    override fun getReader(): BufferedReader =
        BufferedReader(InputStreamReader(ByteArrayInputStream(cachedBody), characterEncoding ?: Charsets.UTF_8.name()))
}
