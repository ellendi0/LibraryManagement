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
        implementation("net.devh:grpc-server-spring-boot-starter:2.15.0.RELEASE")
        implementation("com.salesforce.servicelibs:reactor-grpc-stub:1.2.4")
    }
}
