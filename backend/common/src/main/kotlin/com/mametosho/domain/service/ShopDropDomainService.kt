package com.mametosho.domain.service

import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.repository.CoffeeBeanRepository
import org.springframework.stereotype.Service

/**
 * 店舗の参画落ちに伴うドメインルールを担うサービス。
 *
 * 店舗が参画落ち（DROPPED）になると、その店舗に属する全コーヒー豆を
 * INVALIDATED（無効化）状態に移行する。
 * この操作は不可逆であり、無効化された豆はそれ以降の更新が禁止される。
 */
@Service
class ShopDropDomainService(
    private val coffeeBeanRepository: CoffeeBeanRepository,
) {
    fun invalidateCoffeeBeans(shopId: ShopId) {
        coffeeBeanRepository.invalidateByShopId(shopId)
    }
}
