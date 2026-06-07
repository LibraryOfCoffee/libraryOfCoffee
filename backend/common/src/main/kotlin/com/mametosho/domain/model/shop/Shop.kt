package com.mametosho.domain.model.shop

import com.mametosho.domain.model.shared.Image
import com.mametosho.domain.model.shared.ParticipationStatus
import java.util.UUID

/**
 * 店舗を表す集約ルート。
 *
 * 珈琲豆を提供する店舗の情報を管理する。Shopifyのショップと1対1で紐づく。
 *
 * @property id 店舗ID
 * @property shopifyShopId ShopifyのショップID。システム内で一意
 * @property name 店舗名
 * @property introduction 店舗紹介
 * @property particular こだわり
 * @property shopUrl 店舗URL
 * @property prefecture 都道府県
 * @property participationStatus 参画ステータス（参画前/参画中/参画落ち）
 * @property images 店舗画像一覧
 */
@Suppress("MagicNumber")
data class Shop(
    val id: ShopId,
    val shopifyShopId: ShopifyShopId,
    val name: String,
    val introduction: String?,
    val particular: String?,
    val shopUrl: String,
    val prefecture: Prefecture,
    val participationStatus: ParticipationStatus,
    val images: List<ShopImage>,
) {
    init {
        require(name.isNotBlank()) { "name must not be blank" }
        require(name.length <= 255) { "name must be at most 255 characters, but was ${name.length}" }
        introduction?.let {
            require(it.isNotBlank()) { "introduction must not be blank" }
            require(it.length <= 10000) { "introduction must be at most 10000 characters, but was ${it.length}" }
        }
        particular?.let {
            require(it.isNotBlank()) { "particular must not be blank" }
            require(it.length <= 10000) { "particular must be at most 10000 characters, but was ${it.length}" }
        }
        require(shopUrl.isNotBlank()) { "shopUrl must not be blank" }
        require(shopUrl.length <= 2048) { "shopUrl must be at most 2048 characters, but was ${shopUrl.length}" }
        val logoImageCount = images.count { it.type == ShopImageType.LOGO }
        require(logoImageCount == 1) {
            "LOGO image must be exactly 1, but was $logoImageCount"
        }
    }

    /**
     * 店舗情報を更新する。
     *
     * ShopIdは変更せず、それ以外の項目を置換する。ShopImageIdはUUIDv4を自動再生成する。
     * 参画ステータスの遷移は BEFORE_PARTICIPATION → PARTICIPATING → DROPPED の直線遷移のみ許可。
     * DROPPED 状態からはいかなる変更も不可。
     *
     * @param shopifyShopId ShopifyのショップID
     * @param name 店舗名
     * @param introduction 店舗紹介
     * @param particular こだわり
     * @param shopUrl 店舗URL
     * @param participationStatus 参画ステータス
     * @param images 画像情報（種別とURL）のリスト
     * @return 更新された[Shop]
     */
    fun update(
        shopifyShopId: String,
        name: String,
        introduction: String?,
        particular: String?,
        shopUrl: String,
        prefecture: Prefecture,
        participationStatus: String,
        images: List<Pair<String, String>>,
    ): Shop {
        require(this.participationStatus != ParticipationStatus.DROPPED) {
            "参画落ちの店舗は更新できません"
        }
        val newStatus = ParticipationStatus.valueOf(participationStatus)
        if (newStatus != this.participationStatus) {
            val validNext = mapOf(
                ParticipationStatus.BEFORE_PARTICIPATION to ParticipationStatus.PARTICIPATING,
                ParticipationStatus.PARTICIPATING to ParticipationStatus.DROPPED,
            )
            require(validNext[this.participationStatus] == newStatus) {
                "${this.participationStatus} から $newStatus への遷移は無効です"
            }
        }
        return Shop(
            id = this.id,
            shopifyShopId = ShopifyShopId(shopifyShopId),
            name = name,
            introduction = introduction,
            particular = particular,
            shopUrl = shopUrl,
            prefecture = prefecture,
            participationStatus = newStatus,
            images = images.map { (type, imageUrl) ->
                ShopImage(
                    id = ShopImageId(UUID.randomUUID().toString()),
                    type = ShopImageType.valueOf(type),
                    image = Image(imageUrl),
                )
            },
        )
    }

    companion object {
        /**
         * 新しい店舗を生成する。
         *
         * IDはサーバー側でUUIDv4を自動生成する。
         * 初期ステータスに DROPPED は指定不可。
         *
         * @param shopifyShopId ShopifyのショップID
         * @param name 店舗名
         * @param introduction 店舗紹介
         * @param particular こだわり
         * @param shopUrl 店舗URL
         * @param participationStatus 参画ステータス
         * @param images 画像情報（種別とURL）のリスト
         * @return 生成された[Shop]
         */
        fun create(
            shopifyShopId: String,
            name: String,
            introduction: String?,
            particular: String?,
            shopUrl: String,
            prefecture: Prefecture,
            participationStatus: String,
            images: List<Pair<String, String>>,
            id: String = UUID.randomUUID().toString(),
        ): Shop {
            val status = ParticipationStatus.valueOf(participationStatus)
            require(status != ParticipationStatus.DROPPED) {
                "初期ステータスに DROPPED は指定できません"
            }
            return Shop(
                id = ShopId(id),
                shopifyShopId = ShopifyShopId(shopifyShopId),
                name = name,
                introduction = introduction,
                particular = particular,
                shopUrl = shopUrl,
                prefecture = prefecture,
                participationStatus = status,
                images = images.map { (type, imageUrl) ->
                    ShopImage(
                        id = ShopImageId(UUID.randomUUID().toString()),
                        type = ShopImageType.valueOf(type),
                        image = Image(imageUrl),
                    )
                },
            )
        }
    }
}
