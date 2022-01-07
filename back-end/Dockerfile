FROM openjdk:8-alpine

COPY target/uberjar/back-end.jar /back-end/app.jar

EXPOSE 7410

CMD ["java", "-jar", "/back-end/app.jar"]
