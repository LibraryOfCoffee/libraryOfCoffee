package com.mametosho.infrastructure.persistence.mybatis.mapper

import com.mametosho.infrastructure.persistence.mybatis.entity.SampleEntity
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface SampleMapper {
    @Select("SELECT id, name, created_at, updated_at FROM sample WHERE id = #{id}")
    fun findById(id: Long): SampleEntity?
}
