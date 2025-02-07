FROM eclipse-temurin:17-jdk-jammy

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} backend.jar

RUN mkdir -p src/main/resources
COPY src/main/resources src/main/resources

EXPOSE 8080
ENTRYPOINT ["java","-jar","/backend.jar"]