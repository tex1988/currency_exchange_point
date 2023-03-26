FROM adoptopenjdk/openjdk17:alpine-jre
WORKDIR /workspace
COPY target/*.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/app.jar"]