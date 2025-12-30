plugins {
	kotlin("jvm") version "2.2.21" apply false
	kotlin("plugin.spring") version "2.2.21" apply false
	id("org.springframework.boot") version "4.0.0" apply false
	id("io.spring.dependency-management") version "1.1.7" apply false
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
			languageVersion.set(JavaLanguageVersion.of(24))
		}
	}

	tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
		compilerOptions {
			freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}
