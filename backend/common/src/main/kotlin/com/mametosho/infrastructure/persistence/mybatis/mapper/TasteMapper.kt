package com.mametosho.infrastructure.persistence.mybatis.mapper

import com.mametosho.infrastructure.persistence.mybatis.entity.TasteEntity
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface TasteMapper {
    @Select("SELECT id, name FROM tastes WHERE name = #{name}")
    fun findByName(name: String): TasteEntity?
}
