FROM gradle:latest AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle jar

FROM openjdk:latest

EXPOSE 8080

RUN rm -rf /app && mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/trip-api.jar

ENTRYPOINT ["java", "-jar", "/app/trip-api.jar"]