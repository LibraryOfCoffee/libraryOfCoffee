package com.mametosho.admin.application.query.result

data class CoffeeBeanListResult(
    val id: String,
    val shopId: String,
    val shopifyBeanId: String,
    val name: String,
    val description: String,
    val origin: String,
    val farm: String?,
    val roastLevel: String,
    val processingMethod: String,
    val isSpecialty: Boolean,
)
