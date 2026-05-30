package com.mametosho.infrastructure.persistence.mybatis.mapper

import com.mametosho.infrastructure.persistence.mybatis.entity.PlanEntity
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
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

    @Select(
        """
        <script>
        SELECT id, shopify_plan_id, label, gram_weight, bean_quantity, price, type, is_recommended
        FROM plans
        <where>
            <if test="keyword != null">
                label LIKE CONCAT('%', #{keyword}, '%')
            </if>
        </where>
        ORDER BY bean_quantity ASC, gram_weight ASC
        LIMIT #{size} OFFSET #{offset}
        </script>
        """,
    )
    fun findListRows(
        @Param("size") size: Int,
        @Param("offset") offset: Int,
        @Param("keyword") keyword: String?,
    ): List<PlanEntity>

    @Select(
        """
        <script>
        SELECT COUNT(*) FROM plans
        <where>
            <if test="keyword != null">
                label LIKE CONCAT('%', #{keyword}, '%')
            </if>
        </where>
        </script>
        """,
    )
    fun countByCondition(@Param("keyword") keyword: String?): Long

    @Select(
        """
        SELECT id, shopify_plan_id, label, gram_weight, bean_quantity, price, type, is_recommended
        FROM plans
        WHERE id = #{id}
        """,
    )
    fun findById(id: String): PlanEntity?

    @Insert(
        """
        INSERT INTO plans (id, shopify_plan_id, label, gram_weight, bean_quantity, price, type, is_recommended)
        VALUES (#{id}, #{shopifyPlanId}, #{label}, #{gramWeight}, #{beanQuantity}, #{price}, #{type}, #{isRecommended})
        ON DUPLICATE KEY UPDATE
            shopify_plan_id = VALUES(shopify_plan_id),
            label = VALUES(label),
            gram_weight = VALUES(gram_weight),
            bean_quantity = VALUES(bean_quantity),
            price = VALUES(price),
            type = VALUES(type),
            is_recommended = VALUES(is_recommended)
        """,
    )
    fun upsertPlan(entity: PlanEntity)
}
