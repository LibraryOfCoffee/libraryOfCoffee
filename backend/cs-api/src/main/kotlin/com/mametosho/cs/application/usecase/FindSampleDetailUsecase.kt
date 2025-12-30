package com.mametosho.cs.application.usecase

import com.mametosho.domain.model.Sample
import com.mametosho.domain.repository.SampleRepository
import org.springframework.stereotype.Service

@Service
class FindSampleDetailUsecase(
    private val sampleRepository: SampleRepository
) {
    fun execute(id: Long): Sample? {
        return sampleRepository.findById(id)
    }
}
