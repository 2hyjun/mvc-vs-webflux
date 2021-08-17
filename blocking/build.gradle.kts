plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
}

tasks.getByName("bootJar") {
    version = System.getenv("VERSION") ?: project.version
    enabled = true
}

configurations {
    all {
        exclude(group = "com.fasterxml.jackson.datatype", module = "jackson-datatype-hibernate4")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("io.github.microutils:kotlin-logging:1.8.3")
    api("com.fasterxml.jackson.module:jackson-module-kotlin")
}