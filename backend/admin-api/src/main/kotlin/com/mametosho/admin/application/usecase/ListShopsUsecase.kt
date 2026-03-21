package com.mametosho.admin.application.usecase

import com.mametosho.admin.application.query.ShopQueryService
import com.mametosho.admin.application.query.result.PagedResult
import com.mametosho.admin.application.query.result.ShopListResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ListShopsUsecase(
    private val shopQueryService: ShopQueryService,
) {
    @Transactional(readOnly = true)
    open fun execute(page: Int, size: Int): PagedResult<ShopListResult> {
        return shopQueryService.findList(page, size)
    }
}
