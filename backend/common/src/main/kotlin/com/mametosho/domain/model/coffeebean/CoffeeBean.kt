package com.mametosho.domain.model.coffeebean

import com.mametosho.domain.model.shared.ImageUrl
import com.mametosho.domain.model.shop.ShopId
import com.mametosho.domain.model.taste.TasteId
import java.util.UUID

/**
 * コーヒー豆を表す集約ルート。
 *
 * ショップに属するコーヒー豆の情報を管理する。Shopifyの商品と1対1で紐づく。
 *
 * @property tastes テイスト評価一覧。同一TasteIdの重複は不可
 */
@Suppress("MagicNumber")
data class CoffeeBean(
    val id: CoffeeBeanId,
    val shopId: ShopId,
    val shopifyBeanId: ShopifyBeanId,
    val name: String,
    val description: String,
    val origin: String,
    val farm: String?,
    val roastLevel: RoastLevel,
    val processingMethod: ProcessingMethod,
    val isSpecialty: Boolean,
    val images: List<CoffeeBeanImage>,
    val tastes: List<CoffeeBeanTaste>,
) {
    init {
        require(name.isNotBlank()) { "name must not be blank" }
        require(name.length <= 255) { "name must be at most 255 characters, but was ${name.length}" }
        require(description.isNotBlank()) { "description must not be blank" }
        require(description.length <= 10000) { "description must be at most 10000 characters, but was ${description.length}" }
        require(origin.isNotBlank()) { "origin must not be blank" }
        require(origin.length <= 255) { "origin must be at most 255 characters, but was ${origin.length}" }
        farm?.let {
            require(it.isNotBlank()) { "farm must not be blank" }
            require(it.length <= 255) { "farm must be at most 255 characters, but was ${it.length}" }
        }
        require(images.isNotEmpty()) { "images must not be empty" }
        require(tastes.isNotEmpty()) { "tastes must not be empty" }
        val duplicateTasteIds = tastes.groupBy { it.tasteId }.filter { it.value.size > 1 }.keys
        require(duplicateTasteIds.isEmpty()) {
            "Duplicate tasteId is not allowed: $duplicateTasteIds"
        }
    }

    /**
     * コーヒー豆の情報を更新する。
     *
     * コーヒー豆ID([id])は変更せず、所属ショップを含むそれ以外の情報を更新した新しい[CoffeeBean]を返す。
     * 画像・テイスト評価は全件置換され、子エンティティのIDは新たに自動生成される。
     */
    fun update(
        shopId: String,
        shopifyBeanId: String,
        name: String,
        description: String,
        origin: String,
        farm: String?,
        roastLevel: String,
        processingMethod: String,
        isSpecialty: Boolean,
        images: List<Pair<String, String>>,
        tastes: List<Pair<String, Int>>,
    ): CoffeeBean = CoffeeBean(
        id = this.id,
        shopId = ShopId(shopId),
        shopifyBeanId = ShopifyBeanId(shopifyBeanId),
        name = name,
        description = description,
        origin = origin,
        farm = farm,
        roastLevel = RoastLevel.valueOf(roastLevel),
        processingMethod = ProcessingMethod.valueOf(processingMethod),
        isSpecialty = isSpecialty,
        images = images.map { (type, imageUrl) ->
            CoffeeBeanImage(
                id = CoffeeBeanImageId(UUID.randomUUID().toString()),
                type = CoffeeBeanImageType.valueOf(type),
                imageUrl = ImageUrl(imageUrl),
            )
        },
        tastes = tastes.map { (tasteId, evaluationValue) ->
            CoffeeBeanTaste(
                id = CoffeeBeanTasteId(UUID.randomUUID().toString()),
                tasteId = TasteId(tasteId),
                evaluationValue = evaluationValue,
            )
        },
    )

    companion object {
        /**
         * 新しいコーヒー豆を生成する。
         *
         * IDはサーバー側でUUIDv4を自動生成する。
         */
        fun create(
            shopId: String,
            shopifyBeanId: String,
            name: String,
            description: String,
            origin: String,
            farm: String?,
            roastLevel: String,
            processingMethod: String,
            isSpecialty: Boolean,
            images: List<Pair<String, String>>,
            tastes: List<Pair<String, Int>>,
            id: String = UUID.randomUUID().toString(),
        ): CoffeeBean = CoffeeBean(
            id = CoffeeBeanId(id),
            shopId = ShopId(shopId),
            shopifyBeanId = ShopifyBeanId(shopifyBeanId),
            name = name,
            description = description,
            origin = origin,
            farm = farm,
            roastLevel = RoastLevel.valueOf(roastLevel),
            processingMethod = ProcessingMethod.valueOf(processingMethod),
            isSpecialty = isSpecialty,
            images = images.map { (type, imageUrl) ->
                CoffeeBeanImage(
                    id = CoffeeBeanImageId(UUID.randomUUID().toString()),
                    type = CoffeeBeanImageType.valueOf(type),
                    imageUrl = ImageUrl(imageUrl),
                )
            },
            tastes = tastes.map { (tasteId, evaluationValue) ->
                CoffeeBeanTaste(
                    id = CoffeeBeanTasteId(UUID.randomUUID().toString()),
                    tasteId = TasteId(tasteId),
                    evaluationValue = evaluationValue,
                )
            },
        )
    }
}
