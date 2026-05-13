FROM eclipse-temurin:21-jre
ARG JAR_FILE=target/risk-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]