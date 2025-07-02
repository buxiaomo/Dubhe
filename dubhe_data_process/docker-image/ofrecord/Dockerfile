FROM nvidia/cuda:12.2.2-cudnn8-devel-ubuntu20.04

ENV LANG=C.UTF-8
RUN apt-get update \
    && apt-get install -y --no-install-recommends wget unzip zlib1g-dev zlibc libffi-dev \
    && wget https://www.python.org/ftp/python/3.7.4/Python-3.7.4.tgz -O /tmp/Python-3.7.4.tgz \
    && tar -zxf /tmp/Python-3.7.4.tgz -C /usr/local/src/ \
    && cd /usr/local/src/Python-3.7.4 \
    && ./configure --enable-optimizations --with-lto \
    && make -j$(nproc) && make install \
    && rm -rf /tmp/Python-3.7.4.tgz /usr/local/src/Python-3.7.4 /var/lib/apt/lists/* \
    && ln -s /usr/local/bin/python3.7 /usr/bin/python \
    && ln -s /usr/local/bin/pip3.7 /usr/bin/pip \
    && apt-get clean all

# 安装 oneflow yolov3 依赖
ENV LD_LIBRARY_PATH=/tmp/darknet_dependencies/:$LD_LIBRARY_PATH
RUN apt-get update \
    && apt-get install --no-install-recommends libgl1-mesa-glx libglib2.0-0 -y \
    && wget -q https://media.githubusercontent.com/media/buxiaomo/Dubhe/refs/heads/add-health-check/dubhe_data_process/docker-image/ofrecord/oneflow.zip?download=true -O /tmp/oneflow.zip \
    && unzip /tmp/oneflow.zip -d /tmp \
    && rm -rf /tmp/oneflow.zip \
    && TMPDIR=/tmp/pip pip install --user -i http://mirrors.aliyun.com/pypi/simple/ --trusted-host mirrors.aliyun.com --upgrade pip \
    && TMPDIR=/tmp/pip pip install --user -i http://mirrors.aliyun.com/pypi/simple/ --trusted-host mirrors.aliyun.com /tmp/oneflow_cu102-0.3.4-cp37-cp37m-manylinux2014_x86_64.whl \
    && TMPDIR=/tmp/pip pip install --user -i http://mirrors.aliyun.com/pypi/simple/ --trusted-host mirrors.aliyun.com /tmp/oneflow_yolov3-0.0.0-py3-none-any.whl \
    && TMPDIR=/tmp/pip pip install --user -i http://mirrors.aliyun.com/pypi/simple/ --trusted-host mirrors.aliyun.com opencv-python \
    && TMPDIR=/tmp/pip pip install --user -i http://mirrors.aliyun.com/pypi/simple/ --trusted-host mirrors.aliyun.com numpy \
    && TMPDIR=/tmp/pip pip install --user -i http://mirrors.aliyun.com/pypi/simple/ --trusted-host mirrors.aliyun.com redis \
    && TMPDIR=/tmp/pip pip install --user -i http://mirrors.aliyun.com/pypi/simple/ --trusted-host mirrors.aliyun.com nvidia-ml-py3 \
    && TMPDIR=/tmp/pip pip install protobuf==3.17.0 --user -i http://mirrors.aliyun.com/pypi/simple/ --trusted-host mirrors.aliyun.com \
    && rm -rf /tmp/pip /tmp/*.whl /tmp/oneflow.zip /var/lib/apt/lists/*
