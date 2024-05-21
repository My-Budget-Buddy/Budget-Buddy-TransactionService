FROM eclipse-temurin:17-jre-alpine

RUN apk update && apk upgrade

WORKDIR /app

COPY target/transactionservice-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8083

CMD ["java", "-jar", "app.jar"]