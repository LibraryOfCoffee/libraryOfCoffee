package com.mametosho.cs.infrastructure.persistence.mybatis.mapper

import com.mametosho.cs.infrastructure.persistence.mybatis.entity.ShopListRow
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface ShopQueryMapper {

    @Select("""
        SELECT s.id, s.name, s.introduction, s.shop_url, s.prefecture, si.image_url AS logo_image_url
        FROM shops s
        INNER JOIN shop_images si ON si.shop_id = s.id AND si.type = 'LOGO'
        ORDER BY s.created_at ASC
        LIMIT #{size} OFFSET #{offset}
    """)
    fun findListRows(
        @Param("size") size: Int,
        @Param("offset") offset: Int,
    ): List<ShopListRow>

    @Select("""
        SELECT COUNT(*)
        FROM shops s
        INNER JOIN shop_images si ON si.shop_id = s.id AND si.type = 'LOGO'
    """)
    fun count(): Long
}
