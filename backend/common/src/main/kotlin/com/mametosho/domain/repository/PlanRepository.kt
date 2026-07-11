package com.mametosho.domain.repository

import com.mametosho.domain.model.plan.Plan
import com.mametosho.domain.model.plan.PlanId

interface PlanRepository {
    fun findAll(): List<Plan>
    fun findAll(page: Int, size: Int, keyword: String? = null): Pair<List<Plan>, Long>
    fun findById(id: PlanId): Plan?
    fun save(plan: Plan)
}
