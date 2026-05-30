package com.mametosho.admin.application.usecase

import com.mametosho.admin.application.result.PagedResult
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.repository.ShopRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ListShopsUsecase(
    private val shopRepository: ShopRepository,
) {
    @Transactional(readOnly = true)
    open fun execute(page: Int, size: Int, name: String? = null): PagedResult<Shop> {
        val (shops, totalCount) = shopRepository.findAll(page, size, name)
        return PagedResult(shops, totalCount, page, size)
    }
}
