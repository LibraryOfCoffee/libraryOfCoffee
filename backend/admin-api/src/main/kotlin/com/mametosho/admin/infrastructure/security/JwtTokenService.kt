package com.mametosho.admin.infrastructure.security

import com.mametosho.admin.application.service.TokenGenerator
import com.mametosho.admin.config.JwtProperties
import com.mametosho.domain.model.administrator.Administrator
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.Date

@Component
class JwtTokenService(
    private val jwtProperties: JwtProperties,
) : TokenGenerator {

    override fun generate(administrator: Administrator): String {
        val now = Instant.now()
        val expiry = now.plusSeconds(jwtProperties.expirationSeconds)

        val header = JWSHeader(JWSAlgorithm.HS256)
        val claims = JWTClaimsSet.Builder()
            .subject(administrator.id.value)
            .claim("email", administrator.email.value)
            .claim("role", administrator.role.name)
            .issueTime(Date.from(now))
            .expirationTime(Date.from(expiry))
            .build()

        val signedJWT = SignedJWT(header, claims)
        val signer = MACSigner(jwtProperties.secretKey.toByteArray())
        signedJWT.sign(signer)

        return signedJWT.serialize()
    }
}
