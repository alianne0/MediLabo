FROM eclipse-temurin:21-jre
ARG JAR_FILE=target/demographics-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]

# --# 1. Make Postgres listen on all interfaces
# sed -i '' "s/#listen_addresses = 'localhost'/listen_addresses = '*'/" /opt/homebrew/var/postgresql@18/postgresql.conf

# # 2. Restart Postgres
# brew services restart postgresql@18