package com.mametosho.cs.infrastructure.persistence.mybatis.mapper

import com.mametosho.cs.infrastructure.persistence.mybatis.entity.PlanListRow
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface PlanQueryMapper {

    @Select("""
        SELECT id, label, gram_weight, bean_quantity, price, type, is_recommended
        FROM plans
        ORDER BY bean_quantity ASC, gram_weight ASC
    """)
    fun findListRows(): List<PlanListRow>
}
