package com.mametosho.admin

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.mametosho"])
@MapperScan("com.mametosho.infrastructure.persistence.mybatis.mapper")
class AdminApiApplication

fun main(args: Array<String>) {
	runApplication<AdminApiApplication>(*args)
}
