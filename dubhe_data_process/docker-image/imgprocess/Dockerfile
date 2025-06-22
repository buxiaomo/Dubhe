FROM python:3.6.3
COPY sources.list /etc/apt/sources.list

# RUN DEBIAN_FRONTEND=noninteractive apt-get update 

ENV LANG=C.UTF-8

RUN pip install --user -i https://mirrors.cloud.tencent.com/pypi/simple/ --upgrade pip

RUN pip install --user -i https://mirrors.cloud.tencent.com/pypi/simple/  opencv-python-headless==4.6.0.66

RUN pip install --user -i https://mirrors.cloud.tencent.com/pypi/simple/  numpy==1.19.5

RUN pip install --user -i https://mirrors.cloud.tencent.com/pypi/simple/  pytest-shutil==1.7.0

RUN pip install --user -i https://mirrors.cloud.tencent.com/pypi/simple/  redis==4.3.3

RUN echo 'success!!!'