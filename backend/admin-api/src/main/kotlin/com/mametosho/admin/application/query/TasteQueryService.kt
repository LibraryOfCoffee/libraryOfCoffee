package com.mametosho.admin.application.query

import com.mametosho.admin.application.query.result.TasteListResult

interface TasteQueryService {
    fun findAll(): List<TasteListResult>
}
