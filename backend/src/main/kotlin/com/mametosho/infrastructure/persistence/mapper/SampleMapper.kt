package com.mametosho.infrastructure.persistence.mapper

import com.mametosho.infrastructure.persistence.entity.SampleEntity
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Mapper

@Mapper
interface SampleMapper {
    @Select("SELECT id, name, created_at, updated_at FROM sample WHERE id = #{id}")
    fun findById(id: Long): SampleEntity?
}
