FROM openjdk:21-ea-31 AS final

COPY build/libs/library-management-app-0.0.1-SNAPSHOT-boot.jar library.jar
ENTRYPOINT ["java", "-jar", "library.jar"]
