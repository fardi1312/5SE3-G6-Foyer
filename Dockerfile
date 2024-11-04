# Use an official Java runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven build output (jar file) to the container
COPY target/Foyer-0.0.1-SNAPSHOT.jar foyer-app.jar

# Make port 8412 available to the world outside this container
EXPOSE 8412

# Run the jar file
ENTRYPOINT ["java", "-jar", "foyer-app.jar"]
