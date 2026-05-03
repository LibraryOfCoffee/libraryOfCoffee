plugins {
	`java-library`
	alias(libs.plugins.kotlin.spring)
	alias(libs.plugins.spring.dependency.management)
}

group = "com.mametosho"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
}

configure<JavaPluginExtension> {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(25))
	}
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	compilerOptions {
		jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_25)
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

dependencyManagement {
	imports {
		mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
	}
}

dependencies {
	implementation(libs.spring.boot.starter.web)
	api(libs.spring.boot.starter.security)
	api(libs.spring.boot.starter.oauth2.resource.server)
	implementation(libs.kotlin.reflect)
	api(libs.mybatis.spring.boot.starter)
	runtimeOnly(libs.mysql.connector.j)
	api(libs.aws.s3)

	testImplementation(libs.spring.boot.starter.test)
	testImplementation(libs.kotlin.test.junit5)
	testImplementation(libs.testcontainers.mysql)
	testImplementation(libs.testcontainers.junit.jupiter)
	testRuntimeOnly(libs.junit.platform.launcher)
}
