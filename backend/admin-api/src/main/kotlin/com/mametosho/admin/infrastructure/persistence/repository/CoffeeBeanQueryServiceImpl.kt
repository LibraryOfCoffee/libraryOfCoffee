package com.mametosho.admin.infrastructure.persistence.repository

import com.mametosho.admin.application.query.CoffeeBeanQueryService
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
}
