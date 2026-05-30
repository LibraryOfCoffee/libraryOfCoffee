package com.mametosho.infrastructure.persistence.repository

import com.mametosho.domain.model.plan.Plan
import com.mametosho.domain.model.plan.PlanId
import com.mametosho.domain.model.plan.PlanType
import com.mametosho.domain.model.plan.ShopifyPlanId
import com.mametosho.domain.repository.PlanRepository
import com.mametosho.infrastructure.persistence.mybatis.mapper.PlanMapper
import org.springframework.stereotype.Repository

@Repository
class PlanRepositoryImpl(
    private val planMapper: PlanMapper,
) : PlanRepository {

    override fun findAll(): List<Plan> {
        return planMapper.findAll().map { entity ->
            Plan(
                id = PlanId(entity.id),
                shopifyPlanId = ShopifyPlanId(entity.shopifyPlanId),
                label = entity.label,
                gramWeight = entity.gramWeight,
                beanQuantity = entity.beanQuantity,
                price = entity.price,
                type = PlanType.valueOf(entity.type),
                isRecommended = entity.isRecommended,
            )
        }
    }
}
