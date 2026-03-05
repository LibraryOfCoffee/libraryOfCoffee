package com.mametosho.cs.infrastructure.persistence.mybatis.mapper

import com.mametosho.cs.infrastructure.persistence.mybatis.entity.CoffeeListGroupDetailRow
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface CoffeeListGroupQueryMapper {

    @Select(
        """
        SELECT
            clg.id               AS group_id,
            clg.description      AS group_description,
            cb.id                AS bean_id,
            cb.name              AS bean_name,
            cb.description       AS bean_description,
            cb.origin            AS bean_origin,
            cb.farm              AS bean_farm,
            cb.roast_level       AS bean_roast_level,
            cb.processing_method AS bean_processing_method,
            cb.is_specialty      AS bean_is_specialty,
            cbi.id               AS image_id,
            cbi.type             AS image_type,
            cbi.image_url        AS image_url,
            cbt.id               AS taste_id,
            cbt.tastes_id        AS taste_tastes_id,
            cbt.evaluation_value AS taste_evaluation_value,
            t.name               AS taste_name
        FROM coffee_list_groups clg
        INNER JOIN coffee_list_childs clc ON clg.id = clc.coffee_list_group_id
        INNER JOIN coffee_beans cb ON clc.coffee_bean_id = cb.id
        LEFT JOIN coffee_bean_images cbi ON cb.id = cbi.coffee_bean_id
        LEFT JOIN coffee_bean_tastes cbt ON cb.id = cbt.coffee_bean_id
        INNER JOIN tastes t ON cbt.tastes_id = t.id
        WHERE clg.id = #{id}
        """,
    )
    fun findDetailRowsById(id: String): List<CoffeeListGroupDetailRow>
}
