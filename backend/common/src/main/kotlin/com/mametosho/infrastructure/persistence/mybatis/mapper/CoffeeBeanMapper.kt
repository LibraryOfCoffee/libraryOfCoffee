package com.mametosho.infrastructure.persistence.mybatis.mapper

import com.mametosho.infrastructure.persistence.mybatis.entity.CoffeeBeanEntity
import com.mametosho.infrastructure.persistence.mybatis.entity.CoffeeBeanImageEntity
import com.mametosho.infrastructure.persistence.mybatis.entity.CoffeeBeanTasteEntity
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface CoffeeBeanMapper {
    @Select(
        """
        SELECT id, shop_id, shopify_bean_id, name, description, origin, farm, roast_level, processing_method, is_specialty, publish_status
        FROM coffee_beans WHERE id = #{id}
        """,
    )
    fun findById(id: String): CoffeeBeanEntity?

    @Select("SELECT id, coffee_bean_id, type, image_url FROM coffee_bean_images WHERE coffee_bean_id = #{coffeeBeanId}")
    fun findImagesByCoffeeBeanId(coffeeBeanId: String): List<CoffeeBeanImageEntity>

    @Select("SELECT id, coffee_bean_id, tastes_id, evaluation_value FROM coffee_bean_tastes WHERE coffee_bean_id = #{coffeeBeanId}")
    fun findTastesByCoffeeBeanId(coffeeBeanId: String): List<CoffeeBeanTasteEntity>

    @Insert(
        """
        INSERT INTO coffee_beans (id, shop_id, shopify_bean_id, name, description, origin, farm, roast_level, processing_method, is_specialty, publish_status)
        VALUES (#{id}, #{shopId}, #{shopifyBeanId}, #{name}, #{description}, #{origin}, #{farm}, #{roastLevel}, #{processingMethod}, #{isSpecialty}, #{publishStatus})
        ON DUPLICATE KEY UPDATE
            shop_id = VALUES(shop_id),
            shopify_bean_id = VALUES(shopify_bean_id),
            name = VALUES(name),
            description = VALUES(description),
            origin = VALUES(origin),
            farm = VALUES(farm),
            roast_level = VALUES(roast_level),
            processing_method = VALUES(processing_method),
            is_specialty = VALUES(is_specialty),
            publish_status = VALUES(publish_status)
        """,
    )
    fun upsertCoffeeBean(entity: CoffeeBeanEntity)

    @Delete("DELETE FROM coffee_bean_images WHERE coffee_bean_id = #{coffeeBeanId}")
    fun deleteCoffeeBeanImagesByCoffeeBeanId(coffeeBeanId: String)

    @Insert(
        """
        INSERT INTO coffee_bean_images (id, coffee_bean_id, type, image_url)
        VALUES (#{id}, #{coffeeBeanId}, #{type}, #{imageUrl})
        """,
    )
    fun insertCoffeeBeanImage(entity: CoffeeBeanImageEntity)

    @Delete("DELETE FROM coffee_bean_tastes WHERE coffee_bean_id = #{coffeeBeanId}")
    fun deleteCoffeeBeanTastesByCoffeeBeanId(coffeeBeanId: String)

    @Delete("DELETE FROM coffee_beans WHERE id = #{id}")
    fun deleteCoffeeBeanById(id: String)

    @Update("UPDATE coffee_beans SET publish_status = 'INVALIDATED' WHERE shop_id = #{shopId}")
    fun invalidateByShopId(@Param("shopId") shopId: String)

    @Insert(
        """
        INSERT INTO coffee_bean_tastes (id, coffee_bean_id, tastes_id, evaluation_value)
        VALUES (#{id}, #{coffeeBeanId}, #{tastesId}, #{evaluationValue})
        """,
    )
    fun insertCoffeeBeanTaste(entity: CoffeeBeanTasteEntity)
}
