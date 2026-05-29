package com.mametosho.admin.application.usecase

import com.mametosho.admin.application.query.TasteQueryService
import com.mametosho.admin.application.query.result.TasteListResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ListTastesUsecase(
    private val tasteQueryService: TasteQueryService,
) {
    @Transactional(readOnly = true)
    open fun execute(): List<TasteListResult> {
        return tasteQueryService.findAll()
    }
}
