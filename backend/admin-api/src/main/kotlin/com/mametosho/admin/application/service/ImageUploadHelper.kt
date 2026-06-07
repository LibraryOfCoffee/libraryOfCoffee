package com.mametosho.admin.application.service

import com.mametosho.domain.model.shared.ImagePolicy
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
    require(imageFiles.all { it.size <= ImagePolicy.MAX_FILE_SIZE_BYTES }) {
        "画像ファイルは1MB以下にしてください"
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

data class ExistingImage(val id: String, val type: String, val url: String)

/**
 * keepImageIds に含まれる既存画像を保持し、新規ファイルをアップロードして結合した最終画像リストを返す。
 * keepImageIds に含まれない既存画像は S3 から削除する。
 */
fun ImageStorageService.resolveImages(
    existing: List<ExistingImage>,
    imageFiles: List<MultipartFile>,
    imageTypes: List<String>,
    keepImageIds: List<String>,
    prefix: String,
    entityId: String,
): List<Pair<String, String>> {
    val keepSet = keepImageIds.toSet()
    val (toKeep, toDelete) = existing.partition { it.id in keepSet }
    val newImages = if (imageFiles.isNotEmpty()) {
        uploadImages(prefix, entityId, imageFiles, imageTypes)
    } else {
        emptyList()
    }
    deleteImages(toDelete.map { it.url })
    return toKeep.map { it.type to it.url } + newImages
}
