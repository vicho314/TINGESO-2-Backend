FROM maven:3.8.1-openjdk-17 as build
WORKDIR /build
COPY ../ .
RUN mvn clean package -f parent-pom.xml -DskipTests
RUN mvn clean package -f eureka-server/pom.xml -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /build/eureka-server/target/eureka-server.jar .
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "eureka-server.jar"]
