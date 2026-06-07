package com.mametosho.infrastructure.persistence.repository

import com.mametosho.domain.model.coffeebean.CoffeeBean
import com.mametosho.domain.model.coffeebean.CoffeeBeanId
import com.mametosho.domain.model.coffeebean.CoffeeBeanImage
import com.mametosho.domain.model.coffeebean.CoffeeBeanImageId
import com.mametosho.domain.model.coffeebean.CoffeeBeanImageType
import com.mametosho.domain.model.coffeebean.CoffeeBeanTaste
import com.mametosho.domain.model.coffeebean.CoffeeBeanTasteId
import com.mametosho.domain.model.coffeebean.ProcessingMethod
import com.mametosho.domain.model.coffeebean.RoastLevel
import com.mametosho.domain.model.coffeebean.ShopifyBeanId
import com.mametosho.domain.model.shared.Image
import com.mametosho.domain.model.shared.PublishStatus
import com.mametosho.domain.model.shop.ShopId
import org.springframework.transaction.annotation.Transactional
import com.mametosho.domain.model.taste.TasteId
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
    override fun findById(id: CoffeeBeanId): CoffeeBean? {
        val entity = coffeeBeanMapper.findById(id.value) ?: return null
        val imageEntities = coffeeBeanMapper.findImagesByCoffeeBeanId(id.value)
        val tasteEntities = coffeeBeanMapper.findTastesByCoffeeBeanId(id.value)
        return CoffeeBean(
            id = CoffeeBeanId(entity.id),
            shopId = ShopId(entity.shopId),
            shopifyBeanId = ShopifyBeanId(entity.shopifyBeanId),
            name = entity.name,
            description = entity.description,
            origin = entity.origin,
            farm = entity.farm,
            roastLevel = RoastLevel.valueOf(entity.roastLevel),
            processingMethod = ProcessingMethod.valueOf(entity.processingMethod),
            isSpecialty = entity.isSpecialty,
            publishStatus = PublishStatus.valueOf(entity.publishStatus),
            images = imageEntities.map { img ->
                CoffeeBeanImage(
                    id = CoffeeBeanImageId(img.id),
                    type = CoffeeBeanImageType.valueOf(img.type),
                    image = Image(img.imageUrl),
                )
            },
            tastes = tasteEntities.map { taste ->
                CoffeeBeanTaste(
                    id = CoffeeBeanTasteId(taste.id),
                    tasteId = TasteId(taste.tastesId),
                    evaluationValue = taste.evaluationValue,
                )
            },
        )
    }

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
                roastLevel = coffeeBean.roastLevel.name,
                processingMethod = coffeeBean.processingMethod.name,
                isSpecialty = coffeeBean.isSpecialty,
                publishStatus = coffeeBean.publishStatus.name,
            ),
        )
        coffeeBeanMapper.deleteCoffeeBeanImagesByCoffeeBeanId(coffeeBean.id.value)
        coffeeBean.images.forEach { image ->
            coffeeBeanMapper.insertCoffeeBeanImage(
                CoffeeBeanImageEntity(
                    id = image.id.value,
                    coffeeBeanId = coffeeBean.id.value,
                    type = image.type.name,
                    imageUrl = image.image.url,
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

    override fun deleteById(id: CoffeeBeanId) {
        coffeeBeanMapper.deleteCoffeeBeanImagesByCoffeeBeanId(id.value)
        coffeeBeanMapper.deleteCoffeeBeanTastesByCoffeeBeanId(id.value)
        coffeeBeanMapper.deleteCoffeeBeanById(id.value)
    }

    @Transactional
    override fun invalidateByShopId(shopId: ShopId) {
        coffeeBeanMapper.invalidateByShopId(shopId.value)
    }
}
