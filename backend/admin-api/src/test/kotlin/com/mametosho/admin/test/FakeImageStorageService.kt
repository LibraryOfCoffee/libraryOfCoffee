package com.mametosho.admin.test

import com.mametosho.domain.service.ImageStorageService
import java.io.InputStream

object FakeImageStorageService : ImageStorageService {
    override fun upload(key: String, inputStream: InputStream, contentType: String, contentLength: Long): String =
        "https://example.com/$key"
    override fun delete(key: String) = Unit
    override fun extractKey(imageUrl: String): String? = null
}
