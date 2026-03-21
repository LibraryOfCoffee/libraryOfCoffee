package com.mametosho.admin.application.service

import com.mametosho.domain.service.ImageStorageService
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

fun ImageStorageService.uploadImages(
    prefix: String,
    entityId: String,
    imageFiles: List<MultipartFile>,
    imageTypes: List<String>,
): List<Pair<String, String>> {
    require(imageTypes.size == imageFiles.size) {
        "imageTypes size must match imageFiles size"
    }
    return imageFiles.mapIndexed { index, file ->
        val extension = file.originalFilename?.substringAfterLast('.', "") ?: ""
        val key = "$prefix/$entityId/${UUID.randomUUID()}.$extension"
        val url = upload(
            key = key,
            inputStream = file.inputStream,
            contentType = file.contentType ?: "application/octet-stream",
            contentLength = file.size,
        )
        imageTypes[index] to url
    }
}

fun ImageStorageService.deleteImages(imageUrls: List<String>) {
    for (url in imageUrls) {
        val key = extractKey(url)
        if (key != null) {
            delete(key)
        }
    }
}
