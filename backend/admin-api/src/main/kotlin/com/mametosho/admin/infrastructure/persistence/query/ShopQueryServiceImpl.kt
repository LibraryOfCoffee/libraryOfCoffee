package com.mametosho.admin.infrastructure.persistence.query

import com.mametosho.admin.application.query.ShopQueryService
import com.mametosho.admin.application.query.result.PagedResult
import com.mametosho.admin.application.query.result.ShopListResult
import com.mametosho.infrastructure.persistence.mybatis.mapper.ShopMapper
import org.springframework.stereotype.Service

@Service
class ShopQueryServiceImpl(
    private val shopMapper: ShopMapper,
) : ShopQueryService {

    override fun findList(page: Int, size: Int): PagedResult<ShopListResult> {
        val offset = page * size
        val rows = shopMapper.findListRows(size, offset)
        val totalCount = shopMapper.countAll()

        val items = rows.map { row ->
            ShopListResult(
                id = row.id,
                shopifyShopId = row.shopifyShopId,
                name = row.name,
                introduction = row.introduction,
                particular = row.particular,
                shopUrl = row.shopUrl,
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
