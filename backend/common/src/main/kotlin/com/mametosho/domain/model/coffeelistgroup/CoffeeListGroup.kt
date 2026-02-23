package com.mametosho.domain.model.coffeelistgroup

import com.mametosho.domain.model.coffeebean.CoffeeBeanId

/**
 * 珈琲リストグループを表す集約ルート。
 *
 * 顧客が選択できる珈琲豆の一覧をグループとして管理する。
 * 同一の珈琲豆を重複して含めることはできない。
 *
 * @property id グループID
 * @property description グループの説明
 * @property children 珈琲リスト明細の一覧。同一coffeeBeanIdの重複は不可
 */
data class CoffeeListGroup(
    val id: CoffeeListGroupId,
    val description: String?,
    val children: List<CoffeeListChild>,
) {
    init {
        val duplicateBeanIds = children.groupBy { it.coffeeBeanId }.filter { it.value.size > 1 }.keys
        require(duplicateBeanIds.isEmpty()) {
            "Duplicate coffeeBeanId is not allowed: $duplicateBeanIds"
        }
    }

    /**
     * 珈琲豆を追加する。
     *
     * @param id 新しい明細のID
     * @param coffeeBeanId 追加する珈琲豆のID
     * @return 明細が追加された新しい[CoffeeListGroup]
     * @throws IllegalArgumentException 既に同じ珈琲豆が含まれている場合
     */
    fun addChild(id: CoffeeListChildId, coffeeBeanId: CoffeeBeanId): CoffeeListGroup {
        require(children.none { it.coffeeBeanId == coffeeBeanId }) {
            "CoffeeBean already exists in the group: $coffeeBeanId"
        }
        return copy(children = children + CoffeeListChild(id = id, coffeeBeanId = coffeeBeanId))
    }

    /**
     * 珈琲豆を削除する。
     *
     * @param coffeeBeanId 削除する珈琲豆のID
     * @return 明細が削除された新しい[CoffeeListGroup]
     * @throws IllegalArgumentException 指定した珈琲豆が含まれていない場合
     */
    fun removeChild(coffeeBeanId: CoffeeBeanId): CoffeeListGroup {
        require(children.any { it.coffeeBeanId == coffeeBeanId }) {
            "CoffeeBean not found in the group: $coffeeBeanId"
        }
        return copy(children = children.filter { it.coffeeBeanId != coffeeBeanId })
    }
}
