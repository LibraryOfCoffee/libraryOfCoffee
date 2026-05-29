package com.mametosho.cs.application.usecase

import com.mametosho.cs.application.query.ShopQueryService
import com.mametosho.cs.application.query.result.PagedResult
import com.mametosho.cs.application.query.result.ShopListResult
import org.springframework.stereotype.Service

@Service
class FindShopsUsecase(
    private val shopQueryService: ShopQueryService,
) {
    fun execute(page: Int, size: Int): PagedResult<ShopListResult> {
        return shopQueryService.findList(page, size)
    }
}
