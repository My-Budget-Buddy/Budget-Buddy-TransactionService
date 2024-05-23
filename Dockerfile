FROM alpine:latest

RUN apk update && apk upgrade && apk add openjdk17-jre

WORKDIR /app

COPY target/transactionservice-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8083

CMD ["java", "-jar", "app.jar"]