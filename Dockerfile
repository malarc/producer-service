FROM openjdk:11-jdk-oracle
VOLUME /tmp
WORKDIR /app
COPY service/target/locationdatamaster.jar /app/
ADD --chown=15000:15000 'https://dtdg.co/latest-java-tracer' dd-java-agent.jar
ENTRYPOINT ["java", "-javaagent:dd-java-agent.jar", "-XX:MaxRAMPercentage=50.0", "-jar", "locationdatamaster.jar"]
CMD ["JFR.stop recording=0"]