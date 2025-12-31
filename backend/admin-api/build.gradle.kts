plugins {
	kotlin("plugin.spring")
	id("org.springframework.boot")
	id("io.spring.dependency-management")
	id("org.springdoc.openapi-gradle-plugin")
}

dependencies {
	implementation(project(":common"))

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")

	// OpenAPI / Swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")
	runtimeOnly("com.h2database:h2")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

openApi {
	apiDocsUrl.set("http://localhost:8081/v3/api-docs.yaml")
	outputDir.set(file("${rootProject.projectDir}/../docs/swagger"))
	outputFileName.set("admin-api.yml")
	customBootRun {
		args.set(listOf("--spring.profiles.active=openapi"))
	}
}

tasks.named("forkedSpringBootRun") {
	dependsOn(":common:jar")
}
