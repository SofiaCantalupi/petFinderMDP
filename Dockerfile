# ---------- Stage 1: build ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

# Dependency layer first: re-resolved only when pom.xml changes, not on every code edit.
COPY pom.xml ./
RUN mvn -B -ntp dependency:go-offline

# Source layer.
COPY src ./src
# Tests are skipped: PetFinderApplicationTests is a @SpringBootTest that needs a live MySQL.
# The jar name is derived from the build output, never hardcoded.
RUN mvn -B -ntp clean package -DskipTests \
 && cp "$(find target -maxdepth 1 -type f -name '*.jar' ! -name '*-plain.jar' | head -n 1)" /build/app.jar

# ---------- Stage 2: runtime ----------
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Run unprivileged.
RUN addgroup -S spring && adduser -S spring -G spring
COPY --from=build --chown=spring:spring /build/app.jar /app/app.jar
USER spring

# Container-aware heap sizing; SerialGC suits Render's small/low-CPU instances.
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+UseSerialGC"

# Documentation only — the real port comes from $PORT at runtime.
EXPOSE 8080

# `exec` so the JVM is PID 1 and receives Render's SIGTERM for graceful shutdown.
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
