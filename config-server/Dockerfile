FROM maven:3.8.1-openjdk-17 as build
WORKDIR /build
COPY ../ .
RUN mvn clean package -f parent-pom.xml -DskipTests
RUN mvn clean package -f config-server/pom.xml -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /build/config-server/target/config-server.jar .
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "config-server.jar"]
