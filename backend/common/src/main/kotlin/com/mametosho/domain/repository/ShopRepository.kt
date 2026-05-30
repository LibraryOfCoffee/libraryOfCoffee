package com.mametosho.domain.repository

import com.mametosho.domain.model.shared.PublishStatus
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopId

interface ShopRepository {
    fun save(shop: Shop)
    fun findById(id: ShopId): Shop?

    /**
     * 店舗を一覧取得する。
     *
     * @param publishStatus 公開状態フィルタ。nullの場合は全状態を取得する（管理画面用）。
     *   [PublishStatus.PUBLISHED] を指定すると公開済みのみ取得する（CS用）。
     */
    fun findAll(
        page: Int,
        size: Int,
        name: String? = null,
        publishStatus: PublishStatus? = null,
    ): Pair<List<Shop>, Long>

    fun deleteById(id: ShopId)
}
