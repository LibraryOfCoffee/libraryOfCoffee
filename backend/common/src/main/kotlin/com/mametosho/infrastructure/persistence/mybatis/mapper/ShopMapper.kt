package com.mametosho.infrastructure.persistence.mybatis.mapper

import com.mametosho.infrastructure.persistence.mybatis.entity.ShopEntity
import com.mametosho.infrastructure.persistence.mybatis.entity.ShopImageEntity
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface ShopMapper {
    @Insert(
        """
        INSERT INTO shops (id, shopify_shop_id, name, introduction, particular, shop_url, prefecture)
        VALUES (#{id}, #{shopifyShopId}, #{name}, #{introduction}, #{particular}, #{shopUrl}, #{prefecture})
        ON DUPLICATE KEY UPDATE
            shopify_shop_id = VALUES(shopify_shop_id),
            name = VALUES(name),
            introduction = VALUES(introduction),
            particular = VALUES(particular),
            shop_url = VALUES(shop_url),
            prefecture = VALUES(prefecture)
        """,
    )
    fun upsertShop(entity: ShopEntity)

    @Delete("DELETE FROM shop_images WHERE shop_id = #{shopId}")
    fun deleteShopImagesByShopId(shopId: String)

    @Insert(
        """
        INSERT INTO shop_images (id, shop_id, type, image_url)
        VALUES (#{id}, #{shopId}, #{type}, #{imageUrl})
        """,
    )
    fun insertShopImage(entity: ShopImageEntity)

    @Select("SELECT id, shopify_shop_id, name, introduction, particular, shop_url, prefecture FROM shops WHERE id = #{id}")
    fun findShopById(id: String): ShopEntity?

    @Select("SELECT id, shop_id, type, image_url FROM shop_images WHERE shop_id = #{shopId}")
    fun findShopImagesByShopId(shopId: String): List<ShopImageEntity>

    @Select(
        """
        <script>
        SELECT id, shop_id, type, image_url FROM shop_images
        WHERE shop_id IN
        <foreach item="id" collection="ids" open="(" separator="," close=")">
            #{id}
        </foreach>
        </script>
        """,
    )
    fun findShopImagesByShopIds(@Param("ids") ids: List<String>): List<ShopImageEntity>

    @Delete("DELETE FROM shops WHERE id = #{id}")
    fun deleteShopById(id: String)

    @Select(
        """
        <script>
        SELECT id, shopify_shop_id, name, introduction, particular, shop_url, prefecture
        FROM shops
        <where>
            <if test="name != null">
                name LIKE CONCAT('%', #{name}, '%')
            </if>
        </where>
        ORDER BY created_at DESC
        LIMIT #{size} OFFSET #{offset}
        </script>
        """,
    )
    fun findListRows(
        @Param("size") size: Int,
        @Param("offset") offset: Int,
        @Param("name") name: String?,
    ): List<ShopEntity>

    @Select(
        """
        <script>
        SELECT COUNT(*) FROM shops
        <where>
            <if test="name != null">
                name LIKE CONCAT('%', #{name}, '%')
            </if>
        </where>
        </script>
        """,
    )
    fun countByCondition(@Param("name") name: String?): Long
}
