FROM maven:3.8.1-openjdk-17 as build
WORKDIR /build
COPY ../ .
RUN mvn clean package -f parent-pom.xml -DskipTests
RUN mvn clean package -f tool-service/pom.xml -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /build/tool-service/target/tool-service.jar .
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "tool-service.jar"]
