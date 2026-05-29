package com.mametosho.cs.infrastructure.persistence.query

import com.mametosho.cs.application.query.ShopQueryService
import com.mametosho.cs.application.query.result.PagedResult
import com.mametosho.cs.application.query.result.ShopListResult
import com.mametosho.cs.infrastructure.persistence.mybatis.mapper.ShopQueryMapper
import org.springframework.stereotype.Service

@Service
class ShopQueryServiceImpl(
    private val shopQueryMapper: ShopQueryMapper,
) : ShopQueryService {

    override fun findList(page: Int, size: Int): PagedResult<ShopListResult> {
        val offset = page * size
        val rows = shopQueryMapper.findListRows(size, offset)
        val totalCount = shopQueryMapper.count()

        val items = rows.map { row ->
            ShopListResult(
                id = row.id,
                name = row.name,
                introduction = row.introduction,
                shopUrl = row.shopUrl,
                prefecture = row.prefecture,
                logoImageUrl = row.logoImageUrl,
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
