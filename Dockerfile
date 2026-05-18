# Generic Dockerfile for all backend applications
# This Dockerfile creates a common base that can be reused across all backends
# Build arguments:
# - SOURCE_PATH: Relative path to the backend source code (e.g., "portail-backend", "lotterie/directai-backend")
# - MAIN_CLASS: Main class to run (e.g., "ca.sd.directai.DirectAiApplication")
# - PORT: Port to expose (default: 8080)

FROM eclipse-temurin:25 AS base
ENV TZ=America/Montreal

# Install Maven (common to all backends)
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

FROM base AS dependencies
WORKDIR /app

# Build arguments
ARG SOURCE_PATH

# Use BuildKit cache mounts for Maven dependencies
# No need to copy pom.xml - mount it at build time
RUN --mount=type=bind,source=${SOURCE_PATH}/pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline -B

FROM dependencies AS build
# Build arguments
ARG SOURCE_PATH

# Copy source code
COPY ${SOURCE_PATH}/pom.xml ./pom.xml
COPY ${SOURCE_PATH}/src ./src

# Build the application
RUN mvn dependency:go-offline -B || true
RUN mvn clean package -DskipTests

FROM eclipse-temurin:25-jre AS production
ENV TZ=America/Montreal

# Build arguments for runtime
ARG MAIN_CLASS
ARG PORT=8080

WORKDIR /app

# Install curl for health checks (common to all backends)
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*

# Copy the built JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE ${PORT}

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:${PORT}/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]