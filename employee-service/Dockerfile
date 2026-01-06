FROM maven:3.8.1-openjdk-17 as build
WORKDIR /build
COPY ../ .
RUN mvn clean package -f parent-pom.xml -DskipTests
RUN mvn clean package -f employee-service/pom.xml -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /build/employee-service/target/employee-service.jar .
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "employee-service.jar"]
