# Multi-stage build for user-service (M7)
FROM maven:3.8.1-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY user-service/pom.xml ./user-service/
RUN mvn clean install -DskipTests -f pom.xml

COPY . .
RUN mvn clean package -f user-service/pom.xml -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/user-service/target/user-service-1.0.0.jar ./app.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "app.jar"]
