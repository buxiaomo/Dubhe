ARG REGISTRY_HOST=docker.io
FROM ${REGISTRY_HOST}/library/python:3.6.3

RUN TMPDIR=/usr/local/src/pip pip install --no-cache-dir --user -i https://mirrors.cloud.tencent.com/pypi/simple/ --upgrade pip \
    && rm -rf /usr/local/src/pip

RUN TMPDIR=/usr/local/src/pip pip install --no-cache-dir --user -i https://mirrors.cloud.tencent.com/pypi/simple/  opencv-python-headless==4.6.0.66 \ 
    numpy==1.19.5 \
    pytest-shutil==1.7.0 \
    redis==4.3.3 \
    && rm -rf /usr/local/src/pip

COPY . /app
WORKDIR /app
