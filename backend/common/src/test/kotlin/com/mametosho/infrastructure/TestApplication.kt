package com.mametosho.infrastructure

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@MapperScan("com.mametosho.infrastructure.persistence.mybatis.mapper")
class TestApplication
