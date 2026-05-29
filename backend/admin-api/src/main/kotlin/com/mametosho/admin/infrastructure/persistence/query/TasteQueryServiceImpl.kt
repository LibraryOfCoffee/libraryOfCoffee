package com.mametosho.admin.infrastructure.persistence.query

import com.mametosho.admin.application.query.TasteQueryService
import com.mametosho.admin.application.query.result.TasteListResult
import com.mametosho.infrastructure.persistence.mybatis.mapper.TasteMapper
import org.springframework.stereotype.Service

@Service
class TasteQueryServiceImpl(
    private val tasteMapper: TasteMapper,
) : TasteQueryService {

    override fun findAll(): List<TasteListResult> {
        return tasteMapper.findAll().map { entity ->
            TasteListResult(
                id = entity.id,
                name = entity.name,
            )
        }
    }
}
