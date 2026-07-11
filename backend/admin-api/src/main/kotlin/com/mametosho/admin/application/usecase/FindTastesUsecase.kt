package com.mametosho.admin.application.usecase

import com.mametosho.domain.model.taste.Taste
import com.mametosho.domain.repository.TasteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FindTastesUsecase(
    private val tasteRepository: TasteRepository,
) {
    @Transactional(readOnly = true)
    open fun execute(): List<Taste> {
        return tasteRepository.findAll()
    }
}
