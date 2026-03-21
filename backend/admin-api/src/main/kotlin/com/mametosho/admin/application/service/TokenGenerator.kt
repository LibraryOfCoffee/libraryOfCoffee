package com.mametosho.admin.application.service

import com.mametosho.domain.model.administrator.Administrator

interface TokenGenerator {

    fun generate(administrator: Administrator): String
}
