# Use a lightweight OpenJDK image
FROM openjdk:17-jdk-alpine

# Set working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY target/ats-0.0.1-SNAPSHOT.jar app.jar

# Expose the application's port (default is 8080 for Spring Boot)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
