FROM openjdk:21-ea-31 AS final

COPY build/libs/library-management-0.0.1-SNAPSHOT.jar library.jar
ENTRYPOINT ["java", "-jar", "library.jar"]
