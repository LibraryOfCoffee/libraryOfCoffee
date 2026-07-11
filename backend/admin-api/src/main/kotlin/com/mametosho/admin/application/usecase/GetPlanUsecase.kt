package com.mametosho.admin.application.usecase

import com.mametosho.domain.model.plan.Plan
import com.mametosho.domain.model.plan.PlanId
import com.mametosho.domain.repository.PlanRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetPlanUsecase(
    private val planRepository: PlanRepository,
) {
    @Transactional(readOnly = true)
    open fun execute(id: String): Plan? {
        return planRepository.findById(PlanId(id))
    }
}
