package com.mametosho.infrastructure.persistence.repository

import com.mametosho.domain.model.coffeebean.CoffeeBean
import com.mametosho.domain.repository.CoffeeBeanRepository
import com.mametosho.infrastructure.persistence.mybatis.entity.CoffeeBeanEntity
import com.mametosho.infrastructure.persistence.mybatis.entity.CoffeeBeanImageEntity
import com.mametosho.infrastructure.persistence.mybatis.entity.CoffeeBeanTasteEntity
import com.mametosho.infrastructure.persistence.mybatis.mapper.CoffeeBeanMapper
import org.springframework.stereotype.Repository

@Repository
class CoffeeBeanRepositoryImpl(
    private val coffeeBeanMapper: CoffeeBeanMapper,
) : CoffeeBeanRepository {
    override fun save(coffeeBean: CoffeeBean) {
        coffeeBeanMapper.upsertCoffeeBean(
            CoffeeBeanEntity(
                id = coffeeBean.id.value,
                shopId = coffeeBean.shopId.value,
                shopifyBeanId = coffeeBean.shopifyBeanId.value,
                name = coffeeBean.name,
                description = coffeeBean.description,
                origin = coffeeBean.origin,
                farm = coffeeBean.farm,
                roastLevel = coffeeBean.roastLevel.name.lowercase(),
                processingMethod = coffeeBean.processingMethod.name.lowercase(),
                isSpecialty = coffeeBean.isSpecialty,
            ),
        )
        coffeeBeanMapper.deleteCoffeeBeanImagesByCoffeeBeanId(coffeeBean.id.value)
        coffeeBean.images.forEach { image ->
            coffeeBeanMapper.insertCoffeeBeanImage(
                CoffeeBeanImageEntity(
                    id = image.id.value,
                    coffeeBeanId = coffeeBean.id.value,
                    type = image.type.name.lowercase(),
                    imageUrl = image.imageUrl.value,
                ),
            )
        }
        coffeeBeanMapper.deleteCoffeeBeanTastesByCoffeeBeanId(coffeeBean.id.value)
        coffeeBean.tastes.forEach { taste ->
            coffeeBeanMapper.insertCoffeeBeanTaste(
                CoffeeBeanTasteEntity(
                    id = taste.id.value,
                    coffeeBeanId = coffeeBean.id.value,
                    tastesId = taste.tasteId.value,
                    evaluationValue = taste.evaluationValue,
                ),
            )
        }
    }
}
