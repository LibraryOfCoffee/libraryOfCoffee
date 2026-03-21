package com.mametosho.infrastructure.persistence.repository

import com.mametosho.domain.model.administrator.AdminRole
import com.mametosho.domain.model.administrator.Administrator
import com.mametosho.domain.model.administrator.AdministratorId
import com.mametosho.domain.model.administrator.Email
import com.mametosho.domain.repository.AdministratorRepository
import com.mametosho.infrastructure.persistence.mybatis.mapper.AdministratorMapper
import org.springframework.stereotype.Repository

@Repository
class AdministratorRepositoryImpl(
    private val administratorMapper: AdministratorMapper,
) : AdministratorRepository {

    override fun findByEmail(email: Email): Administrator? {
        val entity = administratorMapper.findByEmail(email.value) ?: return null
        return Administrator(
            id = AdministratorId(entity.id),
            email = Email(entity.email),
            hashedPassword = entity.hashedPassword,
            role = AdminRole.valueOf(entity.role.uppercase()),
        )
    }
}
