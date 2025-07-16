ARG REGISTRY_HOST=docker.io
FROM ${REGISTRY_HOST}/library/nginx:1.27.5-alpine

RUN apk upgrade --no-cache
COPY ./dist/ /usr/share/nginx/html/
COPY nginx.conf /etc/nginx/nginx.conf