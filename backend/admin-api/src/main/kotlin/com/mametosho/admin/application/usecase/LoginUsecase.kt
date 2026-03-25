package com.mametosho.admin.application.usecase

import com.mametosho.admin.application.service.TokenGenerator
import com.mametosho.domain.model.administrator.Email
import com.mametosho.domain.repository.AdministratorRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
open class LoginUsecase(
    private val administratorRepository: AdministratorRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenGenerator: TokenGenerator,
) {

    open fun execute(email: String, password: String): String {
        val administrator = administratorRepository.findByEmail(Email(email))
            ?: throw AuthenticationException()

        if (!passwordEncoder.matches(password, administrator.hashedPassword)) {
            throw AuthenticationException()
        }

        return tokenGenerator.generate(administrator)
    }
}
