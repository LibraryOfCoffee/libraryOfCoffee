package com.mametosho.infrastructure.persistence.mybatis.mapper

import com.mametosho.infrastructure.persistence.mybatis.entity.ShopEntity
import com.mametosho.infrastructure.persistence.mybatis.entity.ShopImageEntity
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper

@Mapper
interface ShopMapper {
    @Insert(
        """
        INSERT INTO shops (id, shopify_shop_id, name, introduction, particular)
        VALUES (#{id}, #{shopifyShopId}, #{name}, #{introduction}, #{particular})
        """,
    )
    fun insertShop(entity: ShopEntity)

    @Insert(
        """
        INSERT INTO shop_images (id, shop_id, type, image_url)
        VALUES (#{id}, #{shopId}, #{type}, #{imageUrl})
        """,
    )
    fun insertShopImage(entity: ShopImageEntity)
}
