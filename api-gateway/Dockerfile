FROM maven:3.8.1-openjdk-17 as build
WORKDIR /build
COPY ../ .
RUN mvn clean package -f parent-pom.xml -DskipTests
RUN mvn clean package -f api-gateway/pom.xml -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /build/api-gateway/target/api-gateway.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]
