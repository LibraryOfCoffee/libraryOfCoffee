plugins {
	alias(libs.plugins.kotlin.spring)
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.spring.dependency.management)
	alias(libs.plugins.springdoc.openapi.gradle)
}

dependencies {
	implementation(project(":common"))

	implementation(libs.spring.boot.starter.web)
	implementation(libs.spring.boot.starter.actuator)
	implementation(libs.kotlin.reflect)
	developmentOnly(libs.spring.boot.docker.compose)

	// OpenAPI / Swagger
	implementation(libs.springdoc.openapi.starter.webmvc.ui)
	runtimeOnly(libs.h2)

	testImplementation(libs.spring.boot.starter.test)
	testImplementation(libs.kotlin.test.junit5)
	testImplementation(libs.archunit.junit5)
	testRuntimeOnly(libs.junit.platform.launcher)
}

openApi {
	apiDocsUrl.set("http://localhost:8080/v3/api-docs.yaml")
	outputDir.set(file("${rootProject.projectDir}/../docs/swagger"))
	outputFileName.set("cs-api.yml")
	customBootRun {
		args.set(listOf("--spring.profiles.active=openapi"))
	}
}

tasks.named("forkedSpringBootRun") {
	dependsOn(":common:jar")
}
