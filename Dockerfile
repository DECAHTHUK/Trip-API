FROM openjdk:17
ARG JAR_FILE=build/libs/trip-api-1.0.0.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]