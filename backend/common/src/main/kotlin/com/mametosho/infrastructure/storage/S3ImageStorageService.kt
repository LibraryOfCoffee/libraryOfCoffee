package com.mametosho.infrastructure.storage

import com.mametosho.domain.service.ImageStorageService
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.InputStream

@Service
class S3ImageStorageService(
    private val s3Client: S3Client,
    private val s3Properties: S3Properties,
) : ImageStorageService {

    override fun upload(key: String, inputStream: InputStream, contentType: String, contentLength: Long): String {
        val putRequest = PutObjectRequest.builder()
            .bucket(s3Properties.bucketName)
            .key(key)
            .contentType(contentType)
            .build()

        s3Client.putObject(putRequest, RequestBody.fromInputStream(inputStream, contentLength))

        return buildUrl(key)
    }

    override fun delete(key: String) {
        val deleteRequest = DeleteObjectRequest.builder()
            .bucket(s3Properties.bucketName)
            .key(key)
            .build()

        s3Client.deleteObject(deleteRequest)
    }

    override fun extractKey(imageUrl: String): String? {
        if (!imageUrl.contains(s3Properties.bucketName)) return null

        return if (!s3Properties.endpoint.isNullOrBlank()) {
            // LocalStack format: {endpoint}/{bucket}/{key}
            val prefix = "${s3Properties.endpoint}/${s3Properties.bucketName}/"
            if (imageUrl.startsWith(prefix)) imageUrl.removePrefix(prefix) else null
        } else {
            // AWS format: https://{bucket}.s3.{region}.amazonaws.com/{key}
            val prefix = "https://${s3Properties.bucketName}.s3.${s3Properties.region}.amazonaws.com/"
            if (imageUrl.startsWith(prefix)) imageUrl.removePrefix(prefix) else null
        }
    }

    private fun buildUrl(key: String): String {
        return if (!s3Properties.endpoint.isNullOrBlank()) {
            "${s3Properties.endpoint}/${s3Properties.bucketName}/$key"
        } else {
            "https://${s3Properties.bucketName}.s3.${s3Properties.region}.amazonaws.com/$key"
        }
    }
}
