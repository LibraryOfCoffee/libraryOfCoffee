plugins {
	`java-library`
	alias(libs.plugins.kotlin.spring)
	alias(libs.plugins.spring.dependency.management)
}

dependencyManagement {
	imports {
		mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
	}
}

dependencies {
	implementation(libs.spring.boot.starter.web)
	implementation(libs.spring.boot.starter.security)
	implementation(libs.kotlin.reflect)
	api(libs.mybatis.spring.boot.starter)
	runtimeOnly(libs.mysql.connector.j)

	testImplementation(libs.spring.boot.starter.test)
	testImplementation(libs.kotlin.test.junit5)
	testImplementation(libs.testcontainers.mysql)
	testImplementation(libs.testcontainers.junit.jupiter)
	testRuntimeOnly(libs.junit.platform.launcher)
}
