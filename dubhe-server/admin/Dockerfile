ARG BASE_IMAGE=docker.io/library/openjdk:8-jre
FROM ${BASE_IMAGE}
RUN apt-get update \
    && apt-get upgrade -y \
    && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY ./target/admin-0.0.1-SNAPSHOT-exec.jar /app/app.jar
ENV SPRING_PROFILES_ACTIVE="prod"
ENV JAVA_OPTION="-Xms1024m -Xmx1024m"
VOLUME [ "/data" ]
CMD ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]