package com.mametosho.admin.application.usecase

import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.repository.ShopRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteShopUsecase(
    private val shopRepository: ShopRepository,
) {
    @Transactional
    fun execute(id: String): Boolean {
        val shopId = ShopId(id)
        shopRepository.findById(shopId) ?: return false
        shopRepository.deleteById(shopId)
        return true
    }
}
