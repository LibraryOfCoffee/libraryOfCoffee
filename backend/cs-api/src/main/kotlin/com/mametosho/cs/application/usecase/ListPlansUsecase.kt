package com.mametosho.cs.application.usecase

import com.mametosho.domain.model.plan.Plan
import com.mametosho.domain.repository.PlanRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ListPlansUsecase(
    private val planRepository: PlanRepository,
) {
    @Transactional(readOnly = true)
    open fun execute(): List<Plan> {
        return planRepository.findAll()
    }
}
