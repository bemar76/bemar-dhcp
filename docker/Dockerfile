# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy

LABEL org.opencontainers.image.authors="Benjamin Mark <bemar@bemar.de>"

RUN mkdir -p /app/logs
RUN chown -R daemon /app
     
ENV LOG_FOLDER=/app/logs

USER daemon
WORKDIR /app

COPY dhcp-server-0.1-SNAPSHOT.jar /app/bemar-dhcp.jar

CMD ["java", "-jar", "/app/bemar-dhcp.jar"]