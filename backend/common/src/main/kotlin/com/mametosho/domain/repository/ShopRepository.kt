package com.mametosho.domain.repository

import com.mametosho.domain.model.shared.ParticipationStatus
import com.mametosho.domain.model.shop.Shop
import com.mametosho.domain.model.shop.ShopId

interface ShopRepository {
    fun save(shop: Shop)
    fun findById(id: ShopId): Shop?

    /**
     * 店舗を一覧取得する。
     *
     * @param participationStatus 参画ステータスフィルタ。nullの場合は全状態を取得する（管理画面用）。
     *   [ParticipationStatus.PARTICIPATING] を指定すると参画中のみ取得する（CS用）。
     */
    fun findAll(
        page: Int,
        size: Int,
        name: String? = null,
        participationStatus: ParticipationStatus? = null,
    ): Pair<List<Shop>, Long>

    fun deleteById(id: ShopId)
}
