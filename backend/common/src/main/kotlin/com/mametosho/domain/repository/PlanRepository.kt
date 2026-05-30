package com.mametosho.domain.repository

import com.mametosho.domain.model.plan.Plan

interface PlanRepository {
    fun findAll(): List<Plan>
}
