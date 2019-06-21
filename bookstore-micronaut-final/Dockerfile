FROM openjdk:8u171-alpine3.7
RUN apk --no-cache add curl
COPY target/bookstore-micronaut*.jar bookstore-micronaut.jar
CMD java ${JAVA_OPTS} -jar bookstore-micronaut.jar