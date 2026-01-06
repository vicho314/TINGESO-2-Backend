# Multi-stage build for reports-service (M6)
FROM maven:3.8.1-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY reports-service/pom.xml ./reports-service/
RUN mvn clean install -DskipTests -f pom.xml

COPY . .
RUN mvn clean package -f reports-service/pom.xml -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/reports-service/target/reports-service-1.0.0.jar ./app.jar
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "app.jar"]
