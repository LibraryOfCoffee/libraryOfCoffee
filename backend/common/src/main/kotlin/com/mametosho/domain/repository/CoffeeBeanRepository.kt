package com.mametosho.domain.repository

import com.mametosho.domain.model.coffeebean.CoffeeBean
import com.mametosho.domain.model.coffeebean.CoffeeBeanId
import com.mametosho.domain.model.shop.ShopId

interface CoffeeBeanRepository {
    fun save(coffeeBean: CoffeeBean)
    fun findById(id: CoffeeBeanId): CoffeeBean?
    fun deleteById(id: CoffeeBeanId)

    /**
     * 指定した店舗に属する全コーヒー豆の公開ステータスを INVALIDATED に更新する。
     * 店舗が参画落ちになった際にシステムが自動的に呼び出す。
     */
    fun invalidateByShopId(shopId: ShopId)
}
