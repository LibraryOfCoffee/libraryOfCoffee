package com.mametosho.infrastructure.persistence.repository

import com.mametosho.domain.model.taste.Taste
import com.mametosho.domain.model.taste.TasteId
import com.mametosho.domain.repository.TasteRepository
import com.mametosho.infrastructure.persistence.mybatis.mapper.TasteMapper
import org.springframework.stereotype.Repository

@Repository
class TasteRepositoryImpl(
    private val tasteMapper: TasteMapper,
) : TasteRepository {

    override fun findAll(): List<Taste> {
        return tasteMapper.findAll().map { entity ->
            Taste(
                id = TasteId(entity.id),
                name = entity.name,
            )
        }
    }
}
