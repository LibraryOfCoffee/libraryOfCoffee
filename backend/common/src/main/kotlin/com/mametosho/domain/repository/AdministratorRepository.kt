package com.mametosho.domain.repository

import com.mametosho.domain.model.administrator.Administrator
import com.mametosho.domain.model.administrator.Email

interface AdministratorRepository {

    fun findByEmail(email: Email): Administrator?
}
