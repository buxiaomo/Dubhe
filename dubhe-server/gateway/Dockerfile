FROM openjdk:8-jre
WORKDIR /app
COPY ./target/gateway-0.0.1-SNAPSHOT-exec.jar /app
ENV JAVA_OPTION="-Xms1024m -Xmx1024m"
VOLUME [ "/data" ]
CMD [ "java", "${JAVA_OPTION}", "-jar", "gateway-0.0.1-SNAPSHOT-exec.jar", "--spring.profiles.active=prod" ]
