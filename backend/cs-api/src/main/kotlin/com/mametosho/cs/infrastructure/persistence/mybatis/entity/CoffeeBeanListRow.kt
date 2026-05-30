package com.mametosho.cs.infrastructure.persistence.mybatis.entity

data class CoffeeBeanListRow(
    val id: String,
    val name: String,
    val origin: String,
    val roastLevel: String,
    val processingMethod: String,
    val isSpecialty: Boolean,
    val description: String,
    val imageUrl: String,
    val shopName: String,
    val shopPrefecture: String,
    val tasteName: String,        // INNER JOIN → non-null
    val evaluationValue: Int,     // INNER JOIN → non-null
)
