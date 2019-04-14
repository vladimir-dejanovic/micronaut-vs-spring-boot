FROM openjdk:8u171-alpine3.7
RUN apk --no-cache add curl
COPY target/sample-micronaut*.jar sample-micronaut.jar
CMD java ${JAVA_OPTS} -jar sample-micronaut.jar