package com.mametosho.domain.repository

import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopId

interface ShopRepository {
    fun save(shop: Shop)
    fun findById(id: ShopId): Shop?
    fun deleteById(id: ShopId)
}
