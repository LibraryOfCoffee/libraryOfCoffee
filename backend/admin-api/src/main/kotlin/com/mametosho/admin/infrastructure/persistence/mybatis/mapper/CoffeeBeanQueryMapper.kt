package com.mametosho.admin.infrastructure.persistence.mybatis.mapper

import com.mametosho.admin.infrastructure.persistence.mybatis.entity.CoffeeBeanDetailRow
import com.mametosho.admin.infrastructure.persistence.mybatis.entity.CoffeeBeanListRow
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface CoffeeBeanQueryMapper {

    @Select(
        """
        SELECT
            cb.id AS bean_id, cb.shop_id, s.name AS shop_name, cb.shopify_bean_id, cb.name AS bean_name,
            cb.description, cb.origin, cb.farm, cb.roast_level, cb.processing_method, cb.is_specialty, cb.publish_status,
            cbi.id AS image_id, cbi.type AS image_type, cbi.image_url,
            cbt.id AS taste_eval_id, cbt.tastes_id AS taste_id, t.name AS taste_name, cbt.evaluation_value
        FROM coffee_beans cb
        INNER JOIN shops s ON cb.shop_id = s.id
        LEFT JOIN coffee_bean_images cbi ON cbi.coffee_bean_id = cb.id
        LEFT JOIN coffee_bean_tastes cbt ON cbt.coffee_bean_id = cb.id
        LEFT JOIN tastes t ON cbt.tastes_id = t.id
        WHERE cb.id = #{id}
        ORDER BY t.name
        """,
    )
    fun findDetailRowsById(id: String): List<CoffeeBeanDetailRow>

    @Select(
        """
        SELECT
            cb.id, cb.shop_id, s.name AS shop_name, cb.shopify_bean_id, cb.name, cb.description, cb.origin, cb.farm,
            cb.roast_level, cb.processing_method, cb.is_specialty, cb.publish_status
        FROM coffee_beans cb
        INNER JOIN shops s ON cb.shop_id = s.id
        ORDER BY cb.created_at DESC
        LIMIT #{size} OFFSET #{offset}
        """,
    )
    fun findListRows(@Param("size") size: Int, @Param("offset") offset: Int): List<CoffeeBeanListRow>

    @Select("SELECT COUNT(*) FROM coffee_beans")
    fun countAll(): Long
}
