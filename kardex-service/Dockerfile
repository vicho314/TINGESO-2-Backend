FROM maven:3.8.1-openjdk-17 as build
WORKDIR /build
COPY ../ .
RUN mvn clean package -f parent-pom.xml -DskipTests
RUN mvn clean package -f kardex-service/pom.xml -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /build/kardex-service/target/kardex-service.jar .
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "kardex-service.jar"]
