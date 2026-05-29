package com.mametosho.cs.infrastructure.persistence.query

import com.mametosho.cs.application.query.PlanQueryService
import com.mametosho.cs.application.query.result.PlanListResult
import com.mametosho.cs.infrastructure.persistence.mybatis.mapper.PlanQueryMapper
import org.springframework.stereotype.Service

@Service
class PlanQueryServiceImpl(
    private val planQueryMapper: PlanQueryMapper,
) : PlanQueryService {

    override fun findList(type: String?, gramWeight: Int?): List<PlanListResult> {
        val rows = planQueryMapper.findListRows(type, gramWeight)
        return rows.map { row ->
            PlanListResult(
                id = row.id,
                label = row.label,
                gramWeight = row.gramWeight,
                beanQuantity = row.beanQuantity,
                price = row.price,
                type = row.type,
                isRecommended = row.isRecommended,
            )
        }
    }
}
