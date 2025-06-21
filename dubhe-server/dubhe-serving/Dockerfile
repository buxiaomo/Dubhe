FROM openjdk:8-jre
WORKDIR /app
COPY ./target/dubhe-serving-0.0.1-SNAPSHOT-exec.jar /app/app.jar
ENV SPRING_PROFILES_ACTIVE="prod"
ENV JAVA_OPTION="-Xms1024m -Xmx1024m"
VOLUME [ "/data" ]
CMD ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]