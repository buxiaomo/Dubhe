FROM docker.io/library/alpine:3.22.0
RUN apk add rsync git \
    git clone https://gitee.com/zhijiangtianshu/Dubhe.git /dubhe-code
COPY init_storage.sh /init_storage.sh
CMD ["/bin/sh", "/init_storage.sh"]