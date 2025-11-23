FROM eclipse-temurin:21
COPY build/libs/calculator.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
