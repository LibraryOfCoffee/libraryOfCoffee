package com.mametosho.cs.infrastructure.persistence.mybatis.mapper

import com.mametosho.cs.infrastructure.persistence.mybatis.entity.CoffeeBeanListRow
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface CoffeeBeanQueryMapper {

    @Select("""
        <script>
        SELECT cb.id, cb.shopify_bean_id, cb.name, cb.origin, cb.roast_level, cb.processing_method, cb.is_specialty,
               cb.description, cbi.image_url, s.name AS shop_name, s.prefecture AS shop_prefecture, s.shop_url,
               t.name AS taste_name, cbt.evaluation_value
        FROM (
            SELECT cb_inner.id
            FROM coffee_beans cb_inner
            INNER JOIN shops s_inner ON cb_inner.shop_id = s_inner.id
            INNER JOIN coffee_bean_images cbi_inner
                ON cbi_inner.coffee_bean_id = cb_inner.id AND cbi_inner.type = 'MAIN'
            <where>
                AND cb_inner.publish_status = 'PUBLISHED'
                AND s_inner.participation_status = 'PARTICIPATING'
                <if test="origin != null">AND cb_inner.origin LIKE CONCAT('%', #{origin}, '%')</if>
                <if test="roastLevel != null">AND cb_inner.roast_level = #{roastLevel}</if>
                <if test="prefecture != null">AND s_inner.prefecture = #{prefecture}</if>
            </where>
            ORDER BY cb_inner.created_at DESC
            LIMIT #{size} OFFSET #{offset}
        ) AS paged
        INNER JOIN coffee_beans cb ON cb.id = paged.id
        INNER JOIN shops s ON cb.shop_id = s.id
        INNER JOIN coffee_bean_images cbi ON cbi.coffee_bean_id = cb.id AND cbi.type = 'MAIN'
        INNER JOIN coffee_bean_tastes cbt ON cbt.coffee_bean_id = cb.id
        INNER JOIN tastes t ON cbt.tastes_id = t.id
        ORDER BY cb.created_at DESC
        </script>
    """)
    fun findListRows(
        @Param("size") size: Int,
        @Param("offset") offset: Int,
        @Param("origin") origin: String?,
        @Param("roastLevel") roastLevel: String?,
        @Param("prefecture") prefecture: String?,
    ): List<CoffeeBeanListRow>

    @Select("""
        <script>
        SELECT COUNT(*)
        FROM coffee_beans cb
        INNER JOIN shops s ON cb.shop_id = s.id
        INNER JOIN coffee_bean_images cbi ON cbi.coffee_bean_id = cb.id AND cbi.type = 'MAIN'
        <where>
            AND cb.publish_status = 'PUBLISHED'
            AND s.participation_status = 'PARTICIPATING'
            <if test="origin != null">AND cb.origin LIKE CONCAT('%', #{origin}, '%')</if>
            <if test="roastLevel != null">AND cb.roast_level = #{roastLevel}</if>
            <if test="prefecture != null">AND s.prefecture = #{prefecture}</if>
        </where>
        </script>
    """)
    fun countFiltered(
        @Param("origin") origin: String?,
        @Param("roastLevel") roastLevel: String?,
        @Param("prefecture") prefecture: String?,
    ): Long
}
