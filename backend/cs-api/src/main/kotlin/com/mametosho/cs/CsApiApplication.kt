package com.mametosho.cs

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.mametosho"])
@MapperScan("com.mametosho.infrastructure.persistence.mapper")
class CsApiApplication

fun main(args: Array<String>) {
	runApplication<CsApiApplication>(*args)
}
