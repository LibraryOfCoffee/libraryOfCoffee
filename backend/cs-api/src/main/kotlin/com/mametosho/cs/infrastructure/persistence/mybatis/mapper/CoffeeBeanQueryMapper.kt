package com.mametosho.cs.infrastructure.persistence.mybatis.mapper

import com.mametosho.cs.infrastructure.persistence.mybatis.entity.CoffeeBeanListRow
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface CoffeeBeanQueryMapper {

    @Select("""
        <script>
        SELECT id, name, origin, roast_level, processing_method, is_specialty
        FROM coffee_beans
        <where>
            <if test="origin != null">AND origin LIKE CONCAT('%', #{origin}, '%')</if>
            <if test="roastLevel != null">AND roast_level = #{roastLevel}</if>
            <if test="processingMethod != null">AND processing_method = #{processingMethod}</if>
        </where>
        ORDER BY created_at DESC
        LIMIT #{size} OFFSET #{offset}
        </script>
    """)
    fun findListRows(
        @Param("size") size: Int,
        @Param("offset") offset: Int,
        @Param("origin") origin: String?,
        @Param("roastLevel") roastLevel: String?,
        @Param("processingMethod") processingMethod: String?,
    ): List<CoffeeBeanListRow>

    @Select("""
        <script>
        SELECT COUNT(*)
        FROM coffee_beans
        <where>
            <if test="origin != null">AND origin LIKE CONCAT('%', #{origin}, '%')</if>
            <if test="roastLevel != null">AND roast_level = #{roastLevel}</if>
            <if test="processingMethod != null">AND processing_method = #{processingMethod}</if>
        </where>
        </script>
    """)
    fun countFiltered(
        @Param("origin") origin: String?,
        @Param("roastLevel") roastLevel: String?,
        @Param("processingMethod") processingMethod: String?,
    ): Long
}
