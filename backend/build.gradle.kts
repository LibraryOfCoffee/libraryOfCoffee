plugins {
	kotlin("jvm") version "2.3.0" apply false
	kotlin("plugin.spring") version "2.3.0" apply false
	id("org.springframework.boot") version "4.0.0" apply false
	id("io.spring.dependency-management") version "1.1.7" apply false
	id("org.springdoc.openapi-gradle-plugin") version "1.9.0" apply false
}

val detektVersion = "1.23.8"
val detektCli: Configuration by configurations.creating {
	isCanBeResolved = true
	isCanBeConsumed = false
}

dependencies {
	detektCli("io.gitlab.arturbosch.detekt:detekt-cli:$detektVersion")
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

tasks.register<JavaExec>("detekt") {
	group = "verification"
	description = "Run detekt static analysis on all modules"
	mainClass.set("io.gitlab.arturbosch.detekt.cli.Main")
	classpath = detektCli

	val configFile = file("config/detekt/detekt.yml")
	val inputDirs = subprojects.flatMap { project ->
		listOf(project.file("src/main/kotlin"), project.file("src/test/kotlin"))
			.filter { it.exists() }
	}

	args(
		"--input", inputDirs.joinToString(",") { it.absolutePath },
		"--config", configFile.absolutePath,
		"--build-upon-default-config",
		"--report", "html:${layout.buildDirectory.get().asFile}/reports/detekt/detekt.html",
		"--report", "xml:${layout.buildDirectory.get().asFile}/reports/detekt/detekt.xml",
	)
}

tasks.register<JavaExec>("detektGenerateConfig") {
	group = "verification"
	description = "Generate default detekt configuration"
	mainClass.set("io.gitlab.arturbosch.detekt.cli.Main")
	classpath = detektCli

	args("--generate-config")
	workingDir = file("config/detekt")
}

// OpenAPI 生成タスク（全モジュール）
tasks.register("generateAllOpenApiDocs") {
	group = "documentation"
	description = "Generate OpenAPI specs for all API modules"
	dependsOn(":cs-api:generateOpenApiDocs", ":admin-api:generateOpenApiDocs")
}
