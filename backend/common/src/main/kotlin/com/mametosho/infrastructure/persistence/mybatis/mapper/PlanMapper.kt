package com.mametosho.infrastructure.persistence.mybatis.mapper

import com.mametosho.infrastructure.persistence.mybatis.entity.PlanEntity
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface PlanMapper {

    @Select(
        """
        SELECT id, shopify_plan_id, label, gram_weight, bean_quantity, price, type, is_recommended
        FROM plans
        ORDER BY bean_quantity ASC, gram_weight ASC
        """,
    )
    fun findAll(): List<PlanEntity>
}
