package com.mametosho.cs.infrastructure.persistence.query

import com.mametosho.cs.application.query.CoffeeBeanQueryService
import com.mametosho.cs.application.query.result.CoffeeBeanListResult
import com.mametosho.cs.application.result.PagedResult
import com.mametosho.cs.infrastructure.persistence.mybatis.mapper.CoffeeBeanQueryMapper
import com.mametosho.domain.model.coffeebean.RoastLevel
import com.mametosho.domain.model.shop.Prefecture
import org.springframework.stereotype.Service

@Service
class CoffeeBeanQueryServiceImpl(
    private val coffeeBeanQueryMapper: CoffeeBeanQueryMapper,
) : CoffeeBeanQueryService {

    override fun findList(
        page: Int,
        size: Int,
        origin: String?,
        roastLevel: RoastLevel?,
        prefecture: Prefecture?,
    ): PagedResult<CoffeeBeanListResult> {
        val offset = page * size
        val rows = coffeeBeanQueryMapper.findListRows(size, offset, origin, roastLevel?.name, prefecture?.name)
        val totalCount = coffeeBeanQueryMapper.countFiltered(origin, roastLevel?.name, prefecture?.name)

        val items = rows
            .groupBy { it.id }
            .map { (_, beanRows) ->
                val first = beanRows.first()
                CoffeeBeanListResult(
                    id = first.id,
                    name = first.name,
                    origin = first.origin,
                    roastLevel = first.roastLevel,
                    processingMethod = first.processingMethod,
                    isSpecialty = first.isSpecialty,
                    description = first.description,
                    imageUrl = first.imageUrl,
                    shopName = first.shopName,
                    shopPrefecture = first.shopPrefecture,
                    shopUrl = first.shopUrl,
                    tasteProfiles = beanRows
                        .map { row ->
                            CoffeeBeanListResult.TasteProfileResult(
                                name = row.tasteName,
                                value = row.evaluationValue,
                            )
                        },
                )
            }

        return PagedResult(
            items = items,
            totalCount = totalCount,
            page = page,
            size = size,
        )
    }
}
