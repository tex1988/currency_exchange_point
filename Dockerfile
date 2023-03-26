FROM openjdk:17-jdk-slim
WORKDIR /workspace
COPY target/*.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/app.jar"]