# Build Stage
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /workspace
COPY . .
RUN ./gradlew clean build -x test

# Run Stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /workspace/build/libs/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=dev
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "/app/app.jar"]