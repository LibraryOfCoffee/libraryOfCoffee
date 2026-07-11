package com.mametosho.domain.repository

import com.mametosho.domain.model.taste.Taste

interface TasteRepository {
    fun findAll(): List<Taste>
}
