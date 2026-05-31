package com.mametosho.admin.application.usecase

import com.mametosho.admin.application.result.PagedResult
import com.mametosho.domain.model.plan.Plan
import com.mametosho.domain.repository.PlanRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FindPlansUsecase(
    private val planRepository: PlanRepository,
) {
    @Transactional(readOnly = true)
    open fun execute(page: Int, size: Int, keyword: String? = null): PagedResult<Plan> {
        val (plans, totalCount) = planRepository.findAll(page, size, keyword)
        return PagedResult(plans, totalCount, page, size)
    }
}
