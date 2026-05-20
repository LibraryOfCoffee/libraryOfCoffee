package com.mametosho.cs.infrastructure.persistence.mybatis.mapper

import com.mametosho.cs.infrastructure.persistence.mybatis.entity.CoffeeBeanListRow
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface CoffeeBeanQueryMapper {

    @Select("""
        <script>
        SELECT cb.id, cb.name, cb.origin, cb.roast_level, cb.processing_method, cb.is_specialty
        FROM coffee_beans cb
        INNER JOIN shops s ON cb.shop_id = s.id
        <where>
            <if test="origin != null">AND cb.origin LIKE CONCAT('%', #{origin}, '%')</if>
            <if test="roastLevel != null">AND cb.roast_level = #{roastLevel}</if>
            <if test="prefecture != null">AND s.prefecture = #{prefecture}</if>
        </where>
        ORDER BY cb.created_at DESC
        LIMIT #{size} OFFSET #{offset}
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
        <where>
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
