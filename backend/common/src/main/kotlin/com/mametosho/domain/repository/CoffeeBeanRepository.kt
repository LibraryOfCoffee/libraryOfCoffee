package com.mametosho.domain.repository

import com.mametosho.domain.model.coffeebean.CoffeeBean
import com.mametosho.domain.model.coffeebean.CoffeeBeanId

interface CoffeeBeanRepository {
    fun save(coffeeBean: CoffeeBean)
    fun findById(id: CoffeeBeanId): CoffeeBean?
    fun deleteById(id: CoffeeBeanId)
}
