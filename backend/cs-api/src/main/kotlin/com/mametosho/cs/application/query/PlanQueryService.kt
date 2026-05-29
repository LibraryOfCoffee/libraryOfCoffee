package com.mametosho.cs.application.query

import com.mametosho.cs.application.query.result.PlanListResult

interface PlanQueryService {
    fun findList(): List<PlanListResult>
}
