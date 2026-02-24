# ===== Stage 1: Build =====
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy maven wrapper & pom first (cache dependencies)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source & build
COPY src/ src/
RUN ./mvnw clean package -DskipTests -B

# ===== Stage 2: Run =====
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Non-root user
RUN addgroup -S wacha && adduser -S wacha -G wacha
USER wacha

COPY --from=build /app/target/*.jar app.jar


EXPOSE 8080



ENTRYPOINT ["java", "-jar", "app.jar"]

