plugins {
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("plugin.spring") version "1.9.23"
    kotlin("plugin.jpa") version "1.9.23"
    kotlin("plugin.noarg") version "1.9.23"
    kotlin("plugin.allopen") version "1.9.23"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
    jacoco
}

allOpen {
    annotations("jakarta.persistence.Entity")
}

dependencies {
    // Spring Boot Starter Dependencies
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter")
    // Database Drivers
    runtimeOnly("com.h2database:h2")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.3")


    // Kotlin Dependencies
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.23")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.23")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.mongock:mongock-springboot-v3:5.4.4")
    implementation("io.mongock:mongodb-springdata-v4-driver:5.4.4")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive:3.3.3")

    // Other Dependencies
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("org.jetbrains.kotlin:kotlin-maven-allopen:1.9.23")
    implementation("io.nats:jnats:2.20.2")
//    implementation("com.google.protobuf:protobuf-java:4.28.1")
    implementation(project(":internal-api"))
//    implementation(project(":gateway"))

    // Test Dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:1.13.12")
    testImplementation("io.projectreactor:reactor-test:3.6.9")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    finalizedBy("jacocoTestReport")
}

tasks.withType<JacocoReport> {
    reports {
        xml.required = true
        html.required = true
        csv.required = true
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }

    afterEvaluate {
        classDirectories.setFrom(files(classDirectories.files.map {
            fileTree(it).apply {
                exclude(
                    "com/example/librarymanagement/exception/**",
                    "com/example/librarymanagement/model/**",
                    "com/example/librarymanagement/converter/**",
                    "com/example/librarymanagement/configuration/**",
                )
            }
        }))
    }
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveClassifier.set("boot")
}

tasks.named<Jar>("jar") {
    archiveClassifier.set("")
}
