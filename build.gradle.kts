import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    jacoco
    kotlin("jvm") version "1.9.23"
    id("com.google.protobuf") version "0.9.4"
}

allprojects {
    group = "com.example"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "com.google.protobuf")

    dependencies {
        implementation("io.projectreactor:reactor-core:3.5.11")

        implementation("io.grpc:grpc-core:1.68.0")
        implementation("io.grpc:grpc-protobuf:1.68.0")
        implementation("io.grpc:grpc-netty:1.68.0")
        implementation("io.grpc:grpc-stub:1.68.0")
        implementation("io.grpc:grpc-services:1.68.0")
        implementation("com.salesforce.servicelibs:reactor-grpc-stub:1.2.4")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_21
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
