package com.mametosho.infrastructure.persistence.mybatis.mapper

import com.mametosho.infrastructure.persistence.mybatis.entity.CoffeeBeanEntity
import com.mametosho.infrastructure.persistence.mybatis.entity.CoffeeBeanImageEntity
import com.mametosho.infrastructure.persistence.mybatis.entity.CoffeeBeanTasteEntity
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper

@Mapper
interface CoffeeBeanMapper {
    @Insert(
        """
        INSERT INTO coffee_beans (id, shop_id, shopify_bean_id, name, description, origin, farm, roast_level, processing_method, is_specialty)
        VALUES (#{id}, #{shopId}, #{shopifyBeanId}, #{name}, #{description}, #{origin}, #{farm}, #{roastLevel}, #{processingMethod}, #{isSpecialty})
        """,
    )
    fun insertCoffeeBean(entity: CoffeeBeanEntity)

    @Insert(
        """
        INSERT INTO coffee_bean_images (id, coffee_bean_id, type, image_url)
        VALUES (#{id}, #{coffeeBeanId}, #{type}, #{imageUrl})
        """,
    )
    fun insertCoffeeBeanImage(entity: CoffeeBeanImageEntity)

    @Insert(
        """
        INSERT INTO coffee_bean_tastes (id, coffee_bean_id, tastes_id, evaluation_value)
        VALUES (#{id}, #{coffeeBeanId}, #{tastesId}, #{evaluationValue})
        """,
    )
    fun insertCoffeeBeanTaste(entity: CoffeeBeanTasteEntity)
}
