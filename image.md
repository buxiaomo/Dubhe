# The construction steps depend on the image

    docker buildx --builder multi-platform build --platform=linux/amd64,linux/arm64 -t 192.168.2.228:30002/library/python:3.6.3 --push .

* docker.io/library/python:3.6.3
* docker.io/library/python:3.7.17
* docker.io/library/openjdk:8-jre
* docker.io/library/alpine:3.22.0
* docker.io/library/nginx:1.27.5-alpine
* docker.io/library/maven:3.5-jdk-8
* docker.io/library/node:12.22.4
* docker.io/library/mysql:8.4.5
* docker.io/tensorflow/tensorflow:2.10.0
* docker.io/nvidia/cuda:12.4.1-cudnn-devel-ubuntu20.04
* docker.io/nvidia/cuda:12.6.3-cudnn-devel-ubuntu20.04
* docker.io/nvidia/cuda:12.2.2-cudnn8-devel-ubuntu20.04
* docker.io/nvidia/cuda:11.2.2-cudnn8-devel-ubuntu18.04

# The runtime depends on the image

* docker.io/grafana/grafana:10.2.1
* docker.io/library/busybox:1.37.0
* docker.io/library/redis:5.0.7
* docker.io/minio/mc:RELEASE.2022-07-15T09-20-55Z
* docker.io/minio/minio:RELEASE.2020-04-28T23-56-56Z
* docker.io/nacos/nacos-mysql:5.7
* docker.io/nacos/nacos-server:2.0.3
* docker.io/prom/node-exporter:v1.7.0
* docker.io/prom/prometheus:v2.54.1
* docker.elastic.co/elasticsearch/elasticsearch:7.17.16
* docker.io/buxiaomo/curl:latest
* docker.io/dcm4che/dcm4chee-arc-psql:5.12.0
* docker.io/dcm4che/postgres-dcm4chee:10.0-12
* docker.io/dcm4che/slapd-dcm4chee:2.4.44-12.0
* docker.io/fluent/fluent-bit:1.9.10