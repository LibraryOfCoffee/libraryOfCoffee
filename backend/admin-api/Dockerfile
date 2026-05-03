# Build stage
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /app

COPY gradle gradle
COPY gradlew .
COPY build.gradle.kts .
COPY settings.docker-admin-api.gradle.kts settings.gradle.kts
COPY common common
COPY admin-api admin-api

RUN chmod +x gradlew
RUN ./gradlew :admin-api:bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:25-jre

WORKDIR /app

RUN groupadd -r spring && useradd -r -g spring spring

COPY --from=builder /app/admin-api/build/libs/*.jar app.jar

RUN chown spring:spring app.jar

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
