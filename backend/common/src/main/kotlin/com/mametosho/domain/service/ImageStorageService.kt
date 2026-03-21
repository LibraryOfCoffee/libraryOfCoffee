package com.mametosho.domain.service

import java.io.InputStream

interface ImageStorageService {
    fun upload(key: String, inputStream: InputStream, contentType: String, contentLength: Long): String
    fun delete(key: String)
    fun extractKey(imageUrl: String): String?
}
