FROM openjdk:17-jdk
VOLUME /tmp
COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

EXPOSE 28086