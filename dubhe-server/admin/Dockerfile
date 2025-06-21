FROM openjdk:8-jdk
WORKDIR /app
COPY ./target/admin-0.0.1-SNAPSHOT-exec.jar /app
ENV JAVA_OPTION="-Xms1024m -Xmx1024m"
VOLUME [ "/data" ]
CMD ["sh", "-c", "java ${JAVA_OPTS} -jar admin-0.0.1-SNAPSHOT-exec.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
