package com.mametosho.cs.infrastructure.persistence.mybatis.mapper

import com.mametosho.cs.infrastructure.persistence.mybatis.entity.PlanListRow
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface PlanQueryMapper {

    @Select("""
        <script>
        SELECT id, label, gram_weight, bean_quantity, price, type, is_recommended
        FROM plans
        <where>
            <if test="type != null">AND type = #{type}</if>
            <if test="gramWeight != null">AND gram_weight = #{gramWeight}</if>
        </where>
        ORDER BY bean_quantity ASC, gram_weight ASC
        </script>
    """)
    fun findListRows(
        @Param("type") type: String?,
        @Param("gramWeight") gramWeight: Int?,
    ): List<PlanListRow>
}
