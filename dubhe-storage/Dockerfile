FROM docker.io/library/alpine:3.22.0
RUN apk add rsync
COPY init_storage.sh /init_storage.sh
CMD ["/bin/sh", "/init_storage.sh"]