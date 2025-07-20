# ✅ Use official JDK 21
FROM eclipse-temurin:21-jdk-alpine

# ✅ Set working directory
WORKDIR /app

# ✅ Copy the built jar
COPY target/parking-lot-service-0.0.1-SNAPSHOT.jar app.jar

# ✅ Expose parking-service port
EXPOSE 8080

# ✅ Run with H2 remote console allowed
ENTRYPOINT ["java", "-Dh2.console.webAllowOthers=true", "-jar", "app.jar"]
