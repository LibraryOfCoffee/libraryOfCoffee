package com.mametosho.infrastructure.persistence.mybatis.entity

data class CoffeeBeanDetailRow(
    val beanId: String,
    val shopId: String,
    val shopifyBeanId: String,
    val beanName: String,
    val description: String,
    val origin: String,
    val farm: String?,
    val roastLevel: String,
    val processingMethod: String,
    val isSpecialty: Boolean,
    val publishStatus: String,
    val imageId: String?,
    val imageType: String?,
    val imageUrl: String?,
    val tasteEvalId: String?,
    val tasteId: String?,
    val tasteName: String?,
    val evaluationValue: Int?,
)
