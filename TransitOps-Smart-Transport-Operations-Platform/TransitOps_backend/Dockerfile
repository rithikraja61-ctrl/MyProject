FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src src

RUN chmod +x gradlew

EXPOSE 8080

CMD ["./gradlew", "bootRun", "-x", "test"]
