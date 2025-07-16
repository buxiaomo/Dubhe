ARG REGISTRY_HOST=docker.io
FROM ${REGISTRY_HOST}/library/alpine:3.22.0

RUN apk add --no-cache openssh-server==10.0_p1-r7 openssh-client bash \
    && mkdir /var/run/sshd \
    && sed -ri 's/^#PermitRootLogin\s+.*/PermitRootLogin yes/' /etc/ssh/sshd_config \
    && sed -ri 's/^#UsePAM\s+.*/UsePAM yes/g' /etc/ssh/sshd_config

COPY docker-entrypoint.sh /docker-entrypoint.sh

ENV ROOT_PASSWORD=root

EXPOSE 22

WORKDIR ["/workspace", "/dataset"]

CMD [ "/docker-entrypoint.sh" ]