package com.mametosho.admin.application.service

import com.mametosho.domain.model.shared.Image
import com.mametosho.domain.service.ImageStorageService
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

/** アップロードする画像ファイルとそのタイプの組。並列配列ではなく型で対応関係を保証する。 */
data class ImageUpload(val type: String, val file: MultipartFile)

/**
 * multipartで別々に届く画像ファイルとタイプ配列を、対応関係を保証した [ImageUpload] のリストに変換する。
 * 並列配列を扱うのはこの境界の1箇所だけに閉じ込める。
 */
fun buildImageUploads(files: List<MultipartFile>, types: List<String>): List<ImageUpload> {
    require(files.size == types.size) {
        "imageTypes size must match images size"
    }
    return files.zip(types) { file, type -> ImageUpload(type, file) }
}

fun ImageStorageService.uploadImages(
    prefix: String,
    entityId: String,
    uploads: List<ImageUpload>,
): List<Pair<String, String>> {
    require(uploads.all { it.file.size <= Image.MAX_FILE_SIZE_BYTES }) {
        "画像ファイルは1MB以下にしてください"
    }
    return uploads.map { (type, file) ->
        val extension = file.originalFilename?.substringAfterLast('.', "") ?: ""
        val key = "$prefix/$entityId/${UUID.randomUUID()}.$extension"
        val url = upload(
            key = key,
            inputStream = file.inputStream,
            contentType = file.contentType ?: "application/octet-stream",
            contentLength = file.size,
        )
        type to url
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
    uploads: List<ImageUpload>,
    keepImageIds: List<String>,
    prefix: String,
    entityId: String,
): List<Pair<String, String>> {
    val keepSet = keepImageIds.toSet()
    val (toKeep, toDelete) = existing.partition { it.id in keepSet }
    val newImages = if (uploads.isNotEmpty()) {
        uploadImages(prefix, entityId, uploads)
    } else {
        emptyList()
    }
    deleteImages(toDelete.map { it.url })
    return toKeep.map { it.type to it.url } + newImages
}
