package com.mametosho.domain.repository

import com.mametosho.domain.model.Sample

interface SampleRepository {
    fun findById(id: Long): Sample?
}
