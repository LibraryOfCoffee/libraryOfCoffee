package com.mametosho.cs.infrastructure.persistence.mybatis.mapper

import com.mametosho.cs.infrastructure.persistence.mybatis.entity.ApiClientEntity
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface ApiClientMapper {

    @Select(
        """
        SELECT id, client_id, description, encrypted_secret, is_active
        FROM api_clients
        WHERE client_id = #{clientId} AND is_active = TRUE
        """,
    )
    fun findActiveByClientId(@Param("clientId") clientId: String): ApiClientEntity?
}
