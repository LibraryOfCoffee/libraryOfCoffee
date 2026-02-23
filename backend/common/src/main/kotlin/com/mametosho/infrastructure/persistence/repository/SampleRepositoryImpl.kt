package com.mametosho.infrastructure.persistence.repository

import com.mametosho.domain.model.Sample
import com.mametosho.domain.repository.SampleRepository
import com.mametosho.infrastructure.persistence.mybatis.mapper.SampleMapper
import org.springframework.stereotype.Repository

@Repository
class SampleRepositoryImpl(
    private val sampleMapper: SampleMapper,
) : SampleRepository {
    override fun findById(id: Long): Sample? {
        return sampleMapper.findById(id)?.let {
            Sample(
                id = it.id,
                name = it.name,
            )
        }
    }
}
