package com.mametosho.cs.infrastructure.persistence.repository

import com.mametosho.cs.application.query.CoffeeListGroupQueryService
import com.mametosho.cs.application.query.result.CoffeeBeanDetailResult
import com.mametosho.cs.application.query.result.CoffeeBeanImageDetailResult
import com.mametosho.cs.application.query.result.CoffeeBeanTasteDetailResult
import com.mametosho.cs.application.query.result.CoffeeListGroupDetailResult
import com.mametosho.cs.infrastructure.persistence.mybatis.entity.CoffeeListGroupDetailRow
import com.mametosho.cs.infrastructure.persistence.mybatis.mapper.CoffeeListGroupQueryMapper
import org.springframework.stereotype.Service

@Service
class CoffeeListGroupQueryServiceImpl(
    private val coffeeListGroupQueryMapper: CoffeeListGroupQueryMapper,
) : CoffeeListGroupQueryService {

    override fun findDetailById(id: String): CoffeeListGroupDetailResult? {
        val rows = coffeeListGroupQueryMapper.findDetailRowsById(id)
        if (rows.isEmpty()) return null

        val firstRow = rows.first()

        val coffeeBeans = rows
            .groupBy { it.beanId }
            .map { (_, beanRows) -> toBeanResult(beanRows.first(), beanRows) }

        return CoffeeListGroupDetailResult(
            id = firstRow.groupId,
            description = firstRow.groupDescription,
            coffeeBeans = coffeeBeans,
        )
    }

    private fun toBeanResult(
        bean: CoffeeListGroupDetailRow,
        beanRows: List<CoffeeListGroupDetailRow>,
    ): CoffeeBeanDetailResult {
        return CoffeeBeanDetailResult(
            id = bean.beanId,
            name = bean.beanName,
            description = bean.beanDescription,
            origin = bean.beanOrigin,
            farm = bean.beanFarm,
            roastLevel = bean.beanRoastLevel,
            processingMethod = bean.beanProcessingMethod,
            images = beanRows
                .filter { it.imageId != null }
                .distinctBy { it.imageId }
                .map { row ->
                    CoffeeBeanImageDetailResult(
                        id = checkNotNull(row.imageId) { "imageId must not be null" },
                        type = checkNotNull(row.imageType) { "imageType must not be null" },
                        imageUrl = checkNotNull(row.imageUrl) { "imageUrl must not be null" },
                    )
                },
            tastes = beanRows
                .filter { it.tasteId != null }
                .distinctBy { it.tasteId }
                .map { row ->
                    CoffeeBeanTasteDetailResult(
                        id = checkNotNull(row.tasteId) { "tasteId must not be null" },
                        tastesId = checkNotNull(row.tasteTastesId) { "tasteTastesId must not be null" },
                        evaluationValue = checkNotNull(row.tasteEvaluationValue) { "tasteEvaluationValue must not be null" },
                        name = checkNotNull(row.tasteName) { "tasteName must not be null" },
                    )
                },
        )
    }
}
