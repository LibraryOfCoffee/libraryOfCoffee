package com.mametosho.cs.infrastructure.persistence.mybatis.entity

data class CoffeeBeanListRow(
    val id: String,
    val name: String,
    val origin: String,
    val roastLevel: String,
    val processingMethod: String,
    val isSpecialty: Boolean,
)
