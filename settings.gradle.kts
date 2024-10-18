plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "library-management"
include("library-management-app", "internal-api", "gateway", "grpc-server-starter")
include("library-management-app:core")
include("library-management-app:author")
include("library-management-app:book")
include("library-management-app:bookpresence")
include("library-management-app:journal")
include("library-management-app:library")
include("library-management-app:publisher")
include("library-management-app:reservation")
include("library-management-app:user")
