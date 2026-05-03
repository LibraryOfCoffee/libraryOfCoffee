# Build stage
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /app

COPY gradle gradle
COPY gradlew .
COPY build.gradle.kts .
COPY settings.docker-cs-api.gradle.kts settings.gradle.kts
COPY common common
COPY cs-api cs-api

RUN chmod +x gradlew
RUN ./gradlew :cs-api:bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:25-jre

WORKDIR /app

RUN groupadd -r spring && useradd -r -g spring spring

COPY --from=builder /app/cs-api/build/libs/*.jar app.jar

RUN chown spring:spring app.jar

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
