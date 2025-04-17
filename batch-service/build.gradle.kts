plugins {
    id("org.springframework.boot") version "3.4.2"
}

apply(from = File("${project.rootDir}/gradle/integration.gradle.kts")) // rename if needed

group = "com.dusktildwan"
version = "0.0.1-SNAPSHOT"

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.google.code.gson:gson:2.12.1")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.postgresql:postgresql")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.check {
    dependsOn("integrationTest")
}
