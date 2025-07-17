ARG REGISTRY_HOST=docker.io
FROM ${REGISTRY_HOST}/nvidia/cuda:11.2.2-cudnn8-devel-ubuntu18.04
WORKDIR /workspace
RUN apt-key adv --recv-keys --keyserver keyserver.ubuntu.com A4B469963BF863CC
RUN rm -rf /etc/apt/sources.list.d && apt update && apt install -y python3-pip cmake libprotobuf-dev protobuf-compiler \
    && ln -s /usr/bin/python3 /usr/bin/python
RUN python3 -m pip install --no-cache-dir -i https://pypi.tuna.tsinghua.edu.cn/simple --upgrade pip
RUN python3 -m pip install --no-cache-dir -i https://pypi.tuna.tsinghua.edu.cn/simple tensorflow-gpu==2.1.0
RUN python3 -m pip install --no-cache-dir -i https://pypi.tuna.tsinghua.edu.cn/simple --find-links https://oneflow-static.oss-cn-beijing.aliyuncs.com/staging/dev_serving/pip.index.html oneflow_cu102
RUN python3 -m pip install --no-cache-dir -i https://pypi.tuna.tsinghua.edu.cn/simple torch==1.6.0 torchvision==0.7.0
RUN python3 -m pip install --no-cache-dir -i https://pypi.tuna.tsinghua.edu.cn/simple fastapi
RUN python3 -m pip install --no-cache-dir -i https://pypi.tuna.tsinghua.edu.cn/simple python-multipart
RUN python3 -m pip install --no-cache-dir -i https://pypi.tuna.tsinghua.edu.cn/simple uvicorn
RUN python3 -m pip install --no-cache-dir -i https://pypi.tuna.tsinghua.edu.cn/simple numpy