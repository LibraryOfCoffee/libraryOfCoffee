package com.mametosho.infrastructure.persistence.repository

import com.mametosho.domain.model.plan.Plan
import com.mametosho.domain.model.plan.PlanId
import com.mametosho.domain.model.plan.PlanType
import com.mametosho.domain.model.plan.ShopifyPlanId
import com.mametosho.domain.repository.PlanRepository
import com.mametosho.infrastructure.persistence.mybatis.entity.PlanEntity
import com.mametosho.infrastructure.persistence.mybatis.mapper.PlanMapper
import org.springframework.stereotype.Repository

@Repository
class PlanRepositoryImpl(
    private val planMapper: PlanMapper,
) : PlanRepository {

    override fun findAll(): List<Plan> {
        return planMapper.findAll().map { it.toDomain() }
    }

    override fun findAll(page: Int, size: Int, keyword: String?): Pair<List<Plan>, Long> {
        val offset = page * size
        val rows = planMapper.findListRows(size, offset, keyword)
        val totalCount = planMapper.countByCondition(keyword)
        return Pair(rows.map { it.toDomain() }, totalCount)
    }

    override fun findById(id: PlanId): Plan? {
        return planMapper.findById(id.value)?.toDomain()
    }

    override fun save(plan: Plan) {
        planMapper.upsertPlan(
            PlanEntity(
                id = plan.id.value,
                shopifyPlanId = plan.shopifyPlanId.value,
                label = plan.label,
                gramWeight = plan.gramWeight,
                beanQuantity = plan.beanQuantity,
                price = plan.price,
                type = plan.type.name,
                isRecommended = plan.isRecommended,
            ),
        )
    }

    private fun PlanEntity.toDomain(): Plan = Plan(
        id = PlanId(id),
        shopifyPlanId = ShopifyPlanId(shopifyPlanId),
        label = label,
        gramWeight = gramWeight,
        beanQuantity = beanQuantity,
        price = price,
        type = PlanType.valueOf(type),
        isRecommended = isRecommended,
    )
}
