package com.mametosho.cs.application.query.result

data class PlanListResult(
    val id: String,
    val label: String,
    val gramWeight: Int,
    val beanQuantity: Int,
    val price: Int,
    val type: String,
    val isRecommended: Boolean,
)
