package com.mametosho.domain.repository

import com.mametosho.domain.model.coffeebean.CoffeeBean

interface CoffeeBeanRepository {
    fun save(coffeeBean: CoffeeBean)
}
