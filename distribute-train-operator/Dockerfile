ARG REGISTRY_HOST=docker.io
FROM ${REGISTRY_HOST}/library/openjdk:8-jre
COPY sources.list /etc/apt/sources.list
RUN apt-get update \
    && apt-get upgrade -y \
    && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY ./target/distribute-train-operator-1.0.jar /app/app.jar
ENV SPRING_PROFILES_ACTIVE="prod"
ENV JAVA_OPTION="-Xms1024m -Xmx1024m"
ENV JAR_BALL=""
VOLUME [ "/data" ]
CMD ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar ${JAR_BALL}"]