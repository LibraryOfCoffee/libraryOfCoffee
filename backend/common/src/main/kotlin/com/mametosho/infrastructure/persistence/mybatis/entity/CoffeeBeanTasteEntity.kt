package com.mametosho.infrastructure.persistence.mybatis.entity

data class CoffeeBeanTasteEntity(
    val id: String,
    val coffeeBeanId: String,
    val tastesId: String,
    val evaluationValue: Int,
)
