package com.mametosho.cs.application.usecase

import com.mametosho.domain.model.shared.ParticipationStatus
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.repository.ShopRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetShopUsecase(
    private val shopRepository: ShopRepository,
) {
    @Transactional(readOnly = true)
    open fun execute(id: String): Shop? {
        // CS（一般ユーザー向け）では参画中の店舗のみを公開する。参画前・参画落ち・存在しない場合は null（→ 404）とする。
        return shopRepository.findById(ShopId(id))
            ?.takeIf { it.participationStatus == ParticipationStatus.PARTICIPATING }
    }
}
