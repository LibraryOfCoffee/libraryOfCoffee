package com.mametosho.cs.infrastructure.persistence.query

import com.mametosho.cs.application.query.CoffeeBeanQueryService
import com.mametosho.cs.application.query.result.CoffeeBeanListResult
import com.mametosho.cs.application.query.result.PagedResult
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

        val items = rows.map { row ->
            CoffeeBeanListResult(
                id = row.id,
                name = row.name,
                origin = row.origin,
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
}
