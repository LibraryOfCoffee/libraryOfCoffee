package com.mametosho.infrastructure.persistence.mybatis.entity

data class PlanEntity(
    val id: String,
    val shopifyPlanId: String,
    val label: String,
    val gramWeight: Int,
    val beanQuantity: Int,
    val price: Int,
    val type: String,
    val isRecommended: Boolean,
)
