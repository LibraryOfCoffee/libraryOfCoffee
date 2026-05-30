package com.mametosho.cs.application.usecase

import com.mametosho.cs.application.result.PagedResult
import com.mametosho.domain.model.shared.PublishStatus
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
        // CS（一般ユーザー向け）では公開済みの店舗のみを返す。下書きは除外する。
        val (shops, totalCount) = shopRepository.findAll(page, size, publishStatus = PublishStatus.PUBLISHED)
        return PagedResult(shops, totalCount, page, size)
    }
}
