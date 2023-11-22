# FROM openjdk:11-jdk-oracle
# VOLUME /tmp
# WORKDIR /app
# COPY service/target/delivery-order-producer.jar /app/
# ADD --chown=15000:15000 'https://dtdg.co/latest-java-tracer' dd-java-agent.jar
# ENTRYPOINT ["java", "-javaagent:dd-java-agent.jar", "-XX:MaxRAMPercentage=50.0", "-jar", "delivery-order-producer.jar"]
# CMD ["JFR.stop recording=0"]



# Use an official Maven runtime as a parent image
FROM maven:3.8.4-openjdk-11-slim

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy the POM file and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the application source code
COPY src /target

# Build the application
RUN mvn package -DskipTests

# Run tests
CMD ["mvn", "test"]