# ✅ Use official JDK 21
FROM eclipse-temurin:21-jdk-alpine

# ✅ Set working directory
WORKDIR /app

# ✅ Copy the built jar
COPY target/ticket-service-0.0.1-SNAPSHOT.jar app.jar

# ✅ Expose ticket-service port
EXPOSE 8081

# ✅ Run the application with H2 remote console allowed
ENTRYPOINT ["java", "-Dh2.console.webAllowOthers=true", "-jar", "app.jar"]
