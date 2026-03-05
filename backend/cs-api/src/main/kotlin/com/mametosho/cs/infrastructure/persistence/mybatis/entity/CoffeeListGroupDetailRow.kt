package com.mametosho.cs.infrastructure.persistence.mybatis.entity

/**
 * MyBatisから返されるフラットな行データ。
 *
 * JOINクエリの1行に対応し、Kotlin側でグルーピングして階層構造に組み立てる。
 */
data class CoffeeListGroupDetailRow(
    val groupId: String,
    val groupDescription: String?,
    val beanId: String,
    val beanName: String,
    val beanDescription: String,
    val beanOrigin: String,
    val beanFarm: String?,
    val beanRoastLevel: String,
    val beanProcessingMethod: String,
    val beanIsSpecialty: Boolean,
    val imageId: String?,
    val imageType: String?,
    val imageUrl: String?,
    val tasteId: String?,
    val tasteTastesId: String?,
    val tasteEvaluationValue: Int?,
    val tasteName: String?,
)
