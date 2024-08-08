plugins {
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    kotlin("plugin.noarg") version "1.9.23"
    kotlin("plugin.allopen") version "1.9.23"
}

noArg {
    annotation("jakarta.persistence.Entity")
}

allOpen {
    annotations("jakarta.persistence.Entity")
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    // Spring Boot Starter Dependencies
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter")
    // Database Drivers
    runtimeOnly("com.h2database:h2")
    implementation("mysql:mysql-connector-java:8.0.33")

    // Kotlin Dependencies
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.23")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.23")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation ("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation ("com.fasterxml.jackson.module:jackson-module-kotlin")
    annotationProcessor("org.projectlombok:lombok")

    // Other Dependencies
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("org.jetbrains.kotlin:kotlin-maven-allopen:1.9.23")

    // Test Dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:1.9")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "library"

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}
