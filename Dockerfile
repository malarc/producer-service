# FROM openjdk:11-jdk-oracle
# VOLUME /tmp
# WORKDIR /app
# COPY service/target/delivery-order-producer.jar /app/
# ADD --chown=15000:15000 'https://dtdg.co/latest-java-tracer' dd-java-agent.jar
# ENTRYPOINT ["java", "-javaagent:dd-java-agent.jar", "-XX:MaxRAMPercentage=50.0", "-jar", "delivery-order-producer.jar"]
# CMD ["JFR.stop recording=0"]


# Use an official Maven runtime as a parent image
# FROM maven:3.8.4-openjdk-11-slim
# WORKDIR /usr/src/app
# COPY pom.xml /usr/src/app
# COPY /service/pom.xml /usr/src/app/service/pom.xml
# RUN mvn dependency:go-offline
# COPY .  /src/target
# RUN mvn package -DskipTests
# CMD ["mvn", "test"]


FROM maven:3.8.4-openjdk-11-slim AS MAVEN_BUILD
WORKDIR /build/
COPY pom.xml /build/

COPY /service /build/src/
COPY /service/pom.xml /build/src/

RUN mvn package

FROM openjdk:11-jdk-oracle
WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/docker-boot-intro-0.1.0.jar /app/
ENTRYPOINT ["java", "-jar", "docker-boot-intro-0.1.0.jar"]
