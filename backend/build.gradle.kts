plugins {
	kotlin("jvm") version "2.3.0" apply false
	kotlin("plugin.spring") version "2.3.0" apply false
	id("org.springframework.boot") version "4.0.0" apply false
	id("io.spring.dependency-management") version "1.1.7" apply false
	id("org.springdoc.openapi-gradle-plugin") version "1.9.0" apply false
}

allprojects {
	group = "com.mametosho"
	version = "0.0.1-SNAPSHOT"

	repositories {
		mavenCentral()
	}
}

subprojects {
	apply(plugin = "org.jetbrains.kotlin.jvm")

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
}

// OpenAPI 生成タスク（全モジュール）
tasks.register("generateAllOpenApiDocs") {
	group = "documentation"
	description = "Generate OpenAPI specs for all API modules"
	dependsOn(":cs-api:generateOpenApiDocs", ":admin-api:generateOpenApiDocs")
}
