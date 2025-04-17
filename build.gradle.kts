plugins {
	java
	id("org.springframework.boot") version "3.4.2" apply false
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.dusktildwan"
version = "0.0.1-SNAPSHOT"

allprojects {
	repositories {
		mavenCentral()
	}
}

subprojects {
	apply(plugin = "java")
	apply(plugin = "io.spring.dependency-management")

	java {
		toolchain {
			languageVersion.set(JavaLanguageVersion.of(21))
		}
	}

	dependencies {
		// Add common test dependencies
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	}

	tasks.test {
		useJUnitPlatform()
	}
}
