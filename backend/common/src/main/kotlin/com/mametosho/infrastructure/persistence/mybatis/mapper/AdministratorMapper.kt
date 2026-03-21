package com.mametosho.infrastructure.persistence.mybatis.mapper

import com.mametosho.infrastructure.persistence.mybatis.entity.AdministratorEntity
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface AdministratorMapper {

    @Select("SELECT id, email, hashed_password, role FROM administrators WHERE email = #{email}")
    fun findByEmail(@Param("email") email: String): AdministratorEntity?
}
