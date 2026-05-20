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

    override fun findList(page: Int, size: Int, name: String?): PagedResult<ShopListResult> {
        val offset = page * size
        val rows = shopMapper.findListRows(size, offset, name)
        val totalCount = shopMapper.countByCondition(name)

        val items = rows.map { row ->
            ShopListResult(
                id = row.id,
                shopifyShopId = row.shopifyShopId,
                name = row.name,
                introduction = row.introduction,
                particular = row.particular,
                shopUrl = row.shopUrl,
                prefecture = row.prefecture,
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
