# ----------- Stage 1: Build -----------
FROM maven:3.9.16-eclipse-temurin-21 AS stage-build

WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
COPY pdfSubmissions/farmerApplication.pdf ./pdfSubmissions/
COPY pdfSubmissions/breederApplication.pdf ./pdfSubmissions/
RUN mvn clean package -DskipTests


# ----------- Stage 2: Runtime -----------
FROM eclipse-temurin:21-jre-alpine-3.23

LABEL maintainer="tolisapo"

WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=stage-build --chown=appuser:appgroup /build/target/myElga-0.0.1-SNAPSHOT.jar ./myElga.jar
COPY --from=stage-build --chown=appuser:appgroup /build/pdfSubmissions /app/pdfSubmissions

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/myElga.jar"]