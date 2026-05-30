package com.mametosho.cs.application.usecase

import com.mametosho.cs.application.query.result.PagedResult
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.repository.ShopRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FindShopsUsecase(
    private val shopRepository: ShopRepository,
) {
    @Transactional(readOnly = true)
    open fun execute(page: Int, size: Int): PagedResult<Shop> {
        val (shops, totalCount) = shopRepository.findAll(page, size)
        return PagedResult(
            items = shops,
            totalCount = totalCount,
            page = page,
            size = size,
        )
    }
}
