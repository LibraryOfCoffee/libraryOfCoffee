package com.mametosho.admin.infrastructure.persistence.query

import com.mametosho.admin.application.query.CoffeeBeanQueryService
import com.mametosho.admin.application.query.result.CoffeeBeanDetailResult
import com.mametosho.admin.application.query.result.CoffeeBeanListResult
import com.mametosho.domain.model.PagedResult
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
        val rows = coffeeBeanMapper.findDetailRowsById(id)
        if (rows.isEmpty()) return null

        val first = rows.first()
        return CoffeeBeanDetailResult(
            id = first.beanId,
            shopId = first.shopId,
            shopifyBeanId = first.shopifyBeanId,
            name = first.beanName,
            description = first.description,
            origin = first.origin,
            farm = first.farm,
            roastLevel = first.roastLevel,
            processingMethod = first.processingMethod,
            isSpecialty = first.isSpecialty,
            images = rows
                .filter { it.imageId != null }
                .distinctBy { it.imageId }
                .map { row ->
                    CoffeeBeanDetailResult.ImageResult(
                        id = checkNotNull(row.imageId),
                        type = checkNotNull(row.imageType),
                        imageUrl = checkNotNull(row.imageUrl),
                    )
                },
            tastes = rows
                .filter { it.tasteEvalId != null }
                .distinctBy { it.tasteEvalId }
                .map { row ->
                    CoffeeBeanDetailResult.TasteResult(
                        id = checkNotNull(row.tasteEvalId),
                        tasteName = checkNotNull(row.tasteName),
                        evaluationValue = checkNotNull(row.evaluationValue),
                    )
                },
        )
    }
}
