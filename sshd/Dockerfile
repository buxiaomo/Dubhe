# docker build -t 192.168.1.2:5000/buxiaomo/sshd:latest .
FROM alpine:3.22.0

RUN apk add --no-cache openssh-server==10.0_p1-r7 openssh-client bash \
    && mkdir /var/run/sshd \
    && sed -ri 's/^#PermitRootLogin\s+.*/PermitRootLogin yes/' /etc/ssh/sshd_config \
    && sed -ri 's/^#UsePAM\s+.*/UsePAM yes/g' /etc/ssh/sshd_config

COPY docker-entrypoint.sh /docker-entrypoint.sh

ENV ROOT_PASSWORD=root

EXPOSE 22

WORKDIR /workspace

CMD [ "/docker-entrypoint.sh" ]