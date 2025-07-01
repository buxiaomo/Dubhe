FROM nvidia/cuda:12.2.2-cudnn8-devel-ubuntu20.04

ENV LANG=C.UTF-8
RUN apt-get update \
    && apt-get install -y --no-install-recommends wget unzip zlib1g-dev zlibc libffi-dev \
    && wget https://www.python.org/ftp/python/3.7.4/Python-3.7.4.tgz -O /tmp/Python-3.7.4.tgz \
    && tar -zvxf /tmp/Python-3.7.4.tgz -C /usr/local/src/ \
    && cd /usr/local/src/Python-3.7.4 \
    && ./configure && make && make install \
    && rm -rf /tmp/Python-3.7.4.tgz /usr/local/src/Python-3.7.4 \
    && ln -s /usr/local/bin/python3.7 /usr/bin/python \
    && ln -s /usr/local/bin/pip3.7 /usr/bin/pip

# 安装oneflow yolov3 依赖
ENV LD_LIBRARY_PATH=/tmp/darknet_dependencies/:$LD_LIBRARY_PATH
RUN wget https://media.githubusercontent.com/media/buxiaomo/Dubhe/refs/heads/add-health-check/dubhe_data_process/docker-image/ofrecord/oneflow.zip?download=true -O /tmp/oneflow.zip \
    && unzip /tmp/oneflow.zip -d /tmp \
    && rm -rf /tmp/oneflow.zip \
    && TMPDIR=/usr/local/src/pip pip install --user -i https://mirrors.cloud.tencent.com/pypi/simple/ --upgrade pip \
    && TMPDIR=/usr/local/src/pip pip install --user -i https://mirrors.cloud.tencent.com/pypi/simple/ /tmp/oneflow_cu102-0.3.4-cp37-cp37m-manylinux2014_x86_64.whl \
    && TMPDIR=/usr/local/src/pip pip install --user -i https://mirrors.cloud.tencent.com/pypi/simple/ /tmp/oneflow_yolov3-0.0.0-py3-none-any.whl \
    && TMPDIR=/usr/local/src/pip pip install --user -i https://mirrors.cloud.tencent.com/pypi/simple/ opencv-python \
    && TMPDIR=/usr/local/src/pip pip install --user -i https://mirrors.cloud.tencent.com/pypi/simple/ numpy \
    && TMPDIR=/usr/local/src/pip pip install --user -i https://mirrors.cloud.tencent.com/pypi/simple/ redis \
    && TMPDIR=/usr/local/src/pip pip install --user -i https://mirrors.cloud.tencent.com/pypi/simple/ nvidia-ml-py3 \
    && TMPDIR=/usr/local/src/pip pip install protobuf==3.17.0 --user -i https://mirrors.cloud.tencent.com/pypi/simple/ \
    && rm -rf /usr/local/src/pip /tmp/*.whl /tmp/oneflow.zip
