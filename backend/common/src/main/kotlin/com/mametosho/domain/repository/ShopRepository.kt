package com.mametosho.domain.repository

import com.mametosho.domain.model.shop.Shop

interface ShopRepository {
    fun save(shop: Shop)
}
