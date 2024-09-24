FROM openjdk:21-ea-31 AS final

COPY build/libs/gateway-0.0.1-SNAPSHOT.jar gateway.jar
ENTRYPOINT ["java", "-jar", "gateway.jar"]
