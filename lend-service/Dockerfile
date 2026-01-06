FROM maven:3.8.1-openjdk-17 as build
WORKDIR /build
COPY ../ .
RUN mvn clean package -f parent-pom.xml -DskipTests
RUN mvn clean package -f lend-service/pom.xml -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /build/lend-service/target/lend-service.jar .
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "lend-service.jar"]
