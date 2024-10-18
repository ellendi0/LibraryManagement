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

dependencies {
    implementation(project(":library-management-app:core"))
    implementation(project(":library-management-app:user"))
    implementation(project(":library-management-app:author"))
    implementation(project(":library-management-app:publisher"))
    implementation(project(":library-management-app:book"))
    implementation(project(":library-management-app:reservation"))
    implementation(project(":library-management-app:journal"))
    implementation(project(":library-management-app:library"))
    implementation(project(":library-management-app:bookpresence"))

    // Spring Boot Starter Dependencies
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter")
    // Database Drivers
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
    implementation("org.springframework.kafka:spring-kafka:3.2.4")
    implementation("io.projectreactor.kafka:reactor-kafka:1.3.23")
    implementation("io.confluent:kafka-protobuf-serializer:7.7.1")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:3.3.4")

    // Other Dependencies
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("org.jetbrains.kotlin:kotlin-maven-allopen:1.9.23")
    implementation("io.nats:jnats:2.20.2")
    implementation(project(":internal-api"))

    // Test Dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:1.13.12")
    testImplementation("io.projectreactor:reactor-test:3.6.9")

    repositories {
        mavenCentral()
        maven {
            url = uri("https://packages.confluent.io/maven/")
        }
    }
}

subprojects {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.noarg")
    apply(plugin = "org.jetbrains.kotlin.plugin.allopen")

    dependencies {
        // Spring Boot Starter Dependencies
        implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter")
        // Database Drivers
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
        implementation("org.springframework.kafka:spring-kafka:3.2.4")
        implementation("io.projectreactor.kafka:reactor-kafka:1.3.23")
        implementation("io.confluent:kafka-protobuf-serializer:7.7.1")
        implementation("org.springframework.boot:spring-boot-starter-data-redis:3.3.4")

        // Other Dependencies
        implementation("org.mindrot:jbcrypt:0.4")
        implementation("org.jetbrains.kotlin:kotlin-maven-allopen:1.9.23")
        implementation("io.nats:jnats:2.20.2")
        implementation(project(":internal-api"))

        // Test Dependencies
        testImplementation("org.springframework.boot:spring-boot-starter-webflux")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("io.mockk:mockk:1.13.12")
        testImplementation("io.projectreactor:reactor-test:3.6.9")

        repositories {
            mavenCentral()
            maven {
                url = uri("https://packages.confluent.io/maven/")
            }
        }
    }

    tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
        enabled = false
    }

    tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
        enabled = false
    }

    tasks.test {
        useJUnitPlatform()
    }
}

tasks.test {
    useJUnitPlatform()
}


//tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
//    archiveClassifier.set("boot")
//}
//
//tasks.named<Jar>("jar") {
//    archiveClassifier.set("")
//}
