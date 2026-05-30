package com.mametosho.cs.application.usecase

import com.mametosho.domain.model.PagedResult
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.repository.ShopRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FindShopsUsecase(
    private val shopRepository: ShopRepository,
) {
    @Transactional(readOnly = true)
    open fun execute(page: Int, size: Int): PagedResult<Shop> = shopRepository.findAll(page, size)
}
