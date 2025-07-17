docker.io/library/python:3.6.3
docker.io/library/openjdk:8-jre
docker.io/library/alpine:3.22.0

kubectl  exec  -it -n infra jenkins-swarm-nsbv7 -c jenkins-swarm bash
docker buildx --builder multi-platform build --platform=linux/amd64,linux/arm64 -t 192.168.2.228:30002/prom/node-exporter:v1.7.0 --push .

docker.io/prom/node-exporter:v1.7.0