package com.mametosho.infrastructure.persistence.mybatis.mapper

import com.mametosho.infrastructure.persistence.mybatis.entity.ShopEntity
import com.mametosho.infrastructure.persistence.mybatis.entity.ShopImageEntity
import com.mametosho.infrastructure.persistence.mybatis.entity.ShopListRow
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface ShopMapper {
    @Insert(
        """
        INSERT INTO shops (id, shopify_shop_id, name, introduction, particular, shop_url, prefecture, publish_status)
        VALUES (#{id}, #{shopifyShopId}, #{name}, #{introduction}, #{particular}, #{shopUrl}, #{prefecture}, #{publishStatus})
        ON DUPLICATE KEY UPDATE
            shopify_shop_id = VALUES(shopify_shop_id),
            name = VALUES(name),
            introduction = VALUES(introduction),
            particular = VALUES(particular),
            shop_url = VALUES(shop_url),
            prefecture = VALUES(prefecture),
            publish_status = VALUES(publish_status)
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

    @Select(
        """
        SELECT s.id, s.shopify_shop_id, s.name, s.introduction, s.particular, s.shop_url, s.prefecture, s.publish_status,
               si.id AS image_id, si.type AS image_type, si.image_url
        FROM shops s
        LEFT JOIN shop_images si ON si.shop_id = s.id
        WHERE s.id = #{id}
        """,
    )
    fun findShopById(id: String): List<ShopListRow>

    @Delete("DELETE FROM shops WHERE id = #{id}")
    fun deleteShopById(id: String)

    @Select(
        """
        <script>
        SELECT s.id, s.shopify_shop_id, s.name, s.introduction, s.particular, s.shop_url, s.prefecture, s.publish_status,
               si.id AS image_id, si.type AS image_type, si.image_url
        FROM (
            SELECT id FROM shops
            <where>
                <if test="name != null">
                    AND name LIKE CONCAT('%', #{name}, '%')
                </if>
                <if test="publishStatus != null">
                    AND publish_status = #{publishStatus}
                </if>
            </where>
            ORDER BY created_at DESC
            LIMIT #{size} OFFSET #{offset}
        ) AS paged
        INNER JOIN shops s ON s.id = paged.id
        LEFT JOIN shop_images si ON si.shop_id = s.id
        ORDER BY s.created_at DESC
        </script>
        """,
    )
    fun findListRows(
        @Param("size") size: Int,
        @Param("offset") offset: Int,
        @Param("name") name: String?,
        @Param("publishStatus") publishStatus: String?,
    ): List<ShopListRow>

    @Select(
        """
        <script>
        SELECT COUNT(*) FROM shops
        <where>
            <if test="name != null">
                AND name LIKE CONCAT('%', #{name}, '%')
            </if>
            <if test="publishStatus != null">
                AND publish_status = #{publishStatus}
            </if>
        </where>
        </script>
        """,
    )
    fun countByCondition(
        @Param("name") name: String?,
        @Param("publishStatus") publishStatus: String?,
    ): Long
}
