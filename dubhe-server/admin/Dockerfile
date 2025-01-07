FROM openjdk:11
WORKDIR /data
COPY ./target/admin-0.0.1-SNAPSHOT-exec.jar /data
ENV JAVA_OPTION="-Xms1024m -Xmx1024m"
CMD [ "java", "${JAVA_OPTION}", "-jar", "/data/admin-0.0.1-SNAPSHOT-exec.jar", "--spring.profiles.active=prod" ]