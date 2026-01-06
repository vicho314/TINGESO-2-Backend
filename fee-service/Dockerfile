FROM maven:3.8.1-openjdk-17 as build
WORKDIR /build
COPY ../ .
RUN mvn clean package -f parent-pom.xml -DskipTests
RUN mvn clean package -f fee-service/pom.xml -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /build/fee-service/target/fee-service.jar .
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "fee-service.jar"]
