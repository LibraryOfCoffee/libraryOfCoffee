package com.mametosho.admin.infrastructure.persistence.query

import com.mametosho.admin.application.query.CoffeeBeanQueryService
import com.mametosho.admin.application.query.result.CoffeeBeanDetailResult
import com.mametosho.admin.application.query.result.CoffeeBeanListResult
import com.mametosho.admin.application.query.result.PagedResult
import com.mametosho.infrastructure.persistence.mybatis.mapper.CoffeeBeanMapper
import org.springframework.stereotype.Service

@Service
class CoffeeBeanQueryServiceImpl(
    private val coffeeBeanMapper: CoffeeBeanMapper,
) : CoffeeBeanQueryService {

    override fun findList(page: Int, size: Int): PagedResult<CoffeeBeanListResult> {
        val offset = page * size
        val rows = coffeeBeanMapper.findListRows(size, offset)
        val totalCount = coffeeBeanMapper.countAll()

        val items = rows.map { row ->
            CoffeeBeanListResult(
                id = row.id,
                shopId = row.shopId,
                shopifyBeanId = row.shopifyBeanId,
                name = row.name,
                description = row.description,
                origin = row.origin,
                farm = row.farm,
                roastLevel = row.roastLevel,
                processingMethod = row.processingMethod,
                isSpecialty = row.isSpecialty,
            )
        }

        return PagedResult(
            items = items,
            totalCount = totalCount,
            page = page,
            size = size,
        )
    }

    override fun findDetail(id: String): CoffeeBeanDetailResult? {
        val bean = coffeeBeanMapper.findById(id) ?: return null
        val images = coffeeBeanMapper.findImagesByCoffeeBeanId(id)
        val tastes = coffeeBeanMapper.findTasteDetailsByCoffeeBeanId(id)

        return CoffeeBeanDetailResult(
            id = bean.id,
            shopId = bean.shopId,
            shopifyBeanId = bean.shopifyBeanId,
            name = bean.name,
            description = bean.description,
            origin = bean.origin,
            farm = bean.farm,
            roastLevel = bean.roastLevel,
            processingMethod = bean.processingMethod,
            isSpecialty = bean.isSpecialty,
            images = images.map { img ->
                CoffeeBeanDetailResult.ImageResult(
                    id = img.id,
                    type = img.type,
                    imageUrl = img.imageUrl,
                )
            },
            tastes = tastes.map { taste ->
                CoffeeBeanDetailResult.TasteResult(
                    id = taste.id,
                    tasteName = taste.tasteName,
                    evaluationValue = taste.evaluationValue,
                )
            },
        )
    }
}
