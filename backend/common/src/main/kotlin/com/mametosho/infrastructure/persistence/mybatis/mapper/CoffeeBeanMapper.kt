package com.mametosho.infrastructure.persistence.mybatis.mapper

import com.mametosho.infrastructure.persistence.mybatis.entity.CoffeeBeanEntity
import com.mametosho.infrastructure.persistence.mybatis.entity.CoffeeBeanImageEntity
import com.mametosho.infrastructure.persistence.mybatis.entity.CoffeeBeanTasteDetailRow
import com.mametosho.infrastructure.persistence.mybatis.entity.CoffeeBeanTasteEntity
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface CoffeeBeanMapper {
    @Select(
        """
        SELECT id, shop_id, shopify_bean_id, name, description, origin, farm, roast_level, processing_method, is_specialty
        FROM coffee_beans WHERE id = #{id}
        """,
    )
    fun findById(id: String): CoffeeBeanEntity?

    @Select("SELECT id, coffee_bean_id, type, image_url FROM coffee_bean_images WHERE coffee_bean_id = #{coffeeBeanId}")
    fun findImagesByCoffeeBeanId(coffeeBeanId: String): List<CoffeeBeanImageEntity>

    @Select("SELECT id, coffee_bean_id, tastes_id, evaluation_value FROM coffee_bean_tastes WHERE coffee_bean_id = #{coffeeBeanId}")
    fun findTastesByCoffeeBeanId(coffeeBeanId: String): List<CoffeeBeanTasteEntity>

    @Select(
        """
        SELECT cbt.id, t.name AS taste_name, cbt.evaluation_value
        FROM coffee_bean_tastes cbt
        INNER JOIN tastes t ON cbt.tastes_id = t.id
        WHERE cbt.coffee_bean_id = #{coffeeBeanId}
        """,
    )
    fun findTasteDetailsByCoffeeBeanId(coffeeBeanId: String): List<CoffeeBeanTasteDetailRow>

    @Select(
        """
        SELECT id, shop_id, shopify_bean_id, name, description, origin, farm, roast_level, processing_method, is_specialty
        FROM coffee_beans
        ORDER BY created_at DESC
        LIMIT #{size} OFFSET #{offset}
        """,
    )
    fun findListRows(@Param("size") size: Int, @Param("offset") offset: Int): List<CoffeeBeanEntity>

    @Select("SELECT COUNT(*) FROM coffee_beans")
    fun countAll(): Long

    @Insert(
        """
        INSERT INTO coffee_beans (id, shop_id, shopify_bean_id, name, description, origin, farm, roast_level, processing_method, is_specialty)
        VALUES (#{id}, #{shopId}, #{shopifyBeanId}, #{name}, #{description}, #{origin}, #{farm}, #{roastLevel}, #{processingMethod}, #{isSpecialty})
        ON DUPLICATE KEY UPDATE
            shop_id = VALUES(shop_id),
            shopify_bean_id = VALUES(shopify_bean_id),
            name = VALUES(name),
            description = VALUES(description),
            origin = VALUES(origin),
            farm = VALUES(farm),
            roast_level = VALUES(roast_level),
            processing_method = VALUES(processing_method),
            is_specialty = VALUES(is_specialty)
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

    @Insert(
        """
        INSERT INTO coffee_bean_tastes (id, coffee_bean_id, tastes_id, evaluation_value)
        VALUES (#{id}, #{coffeeBeanId}, #{tastesId}, #{evaluationValue})
        """,
    )
    fun insertCoffeeBeanTaste(entity: CoffeeBeanTasteEntity)
}
