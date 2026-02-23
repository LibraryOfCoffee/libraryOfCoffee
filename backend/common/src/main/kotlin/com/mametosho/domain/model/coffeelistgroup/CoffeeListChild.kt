package com.mametosho.domain.model.coffeelistgroup

import com.mametosho.domain.model.coffeebean.CoffeeBeanId

/**
 * 珈琲リスト明細を表すエンティティ。
 *
 * 珈琲リストグループに属し、1つの珈琲豆への参照を持つ。
 *
 * @property id 明細ID
 * @property coffeeBeanId 珈琲豆のID
 */
data class CoffeeListChild(
    val id: CoffeeListChildId,
    val coffeeBeanId: CoffeeBeanId,
)
