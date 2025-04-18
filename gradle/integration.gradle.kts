import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.*

val javaPlugin = extensions.getByType<JavaPluginExtension>()

val integrationTestSourceSet = javaPlugin.sourceSets.findByName("integrationTest")
    ?: javaPlugin.sourceSets.create("integrationTest") {
        java.srcDir("src/integration/java")
        resources.srcDir("src/integration/resources")
        compileClasspath += javaPlugin.sourceSets["main"].output
        runtimeClasspath += javaPlugin.sourceSets["main"].output
    }

val integrationTestImplementation = configurations.findByName("integrationTestImplementation")
    ?: configurations.create("integrationTestImplementation") {
        extendsFrom(configurations["testImplementation"])
    }

val integrationTestRuntime = configurations.findByName("integrationTestRuntime")
    ?: configurations.create("integrationTestRuntime") {
        extendsFrom(configurations["runtimeOnly"])
    }

dependencies {
    constraints {
        add("implementation", "org.apache.commons:commons-compress:1.26.0")
    }
    add("integrationTestImplementation", project(":common"))

    add("integrationTestImplementation", "io.zonky.test:embedded-database-spring-test:2.6.0") {
        exclude(group = "org.apache.commons", module = "commons-compress")
    }
    add("integrationTestImplementation", "org.assertj:assertj-core:3.25.3")

    add("integrationTestImplementation", "org.postgresql:postgresql")
    add("integrationTestImplementation", "org.junit.jupiter:junit-jupiter:5.7.1")

    add("integrationTestImplementation", "org.springframework.boot:spring-boot-starter-data-jpa")
    add("integrationTestImplementation", "org.springframework.boot:spring-boot-starter-test")

    add("integrationTestRuntime", "org.junit.platform:junit-platform-launcher")
}

tasks.findByName("integrationTest") ?: tasks.register<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"

    testClassesDirs = integrationTestSourceSet.output.classesDirs
    classpath = integrationTestSourceSet.runtimeClasspath

    shouldRunAfter(tasks.named("test"))
    useJUnitPlatform()
}
