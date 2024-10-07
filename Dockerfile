# Use Java 21 base image
FROM openjdk:21-slim

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY target/*.jar app.jar

# Create a volume for the input file
VOLUME /data

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

