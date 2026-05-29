package com.mametosho.cs.infrastructure.persistence.mybatis.entity

data class PlanListRow(
    val id: String,
    val label: String,
    val gramWeight: Int,
    val beanQuantity: Int,
    val price: Int,
    val type: String,
    val isRecommended: Boolean,
)
