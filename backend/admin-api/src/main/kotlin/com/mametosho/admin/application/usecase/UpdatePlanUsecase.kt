package com.mametosho.admin.application.usecase

import com.mametosho.admin.presentation.dto.request.UpdatePlanRequest
import com.mametosho.domain.model.plan.Plan
import com.mametosho.domain.model.plan.PlanId
import com.mametosho.domain.model.plan.PlanType
import com.mametosho.domain.repository.PlanRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdatePlanUsecase(
    private val planRepository: PlanRepository,
) {
    @Transactional
    open fun execute(id: String, request: UpdatePlanRequest): Plan? {
        val existingPlan = planRepository.findById(PlanId(id)) ?: return null

        val updatedPlan = existingPlan.update(
            shopifyPlanId = request.shopifyPlanId,
            label = request.label,
            gramWeight = request.gramWeight,
            beanQuantity = request.beanQuantity,
            price = request.price,
            type = PlanType.valueOf(request.type),
            isRecommended = request.isRecommended,
        )
        planRepository.save(updatedPlan)
        return updatedPlan
    }
}
