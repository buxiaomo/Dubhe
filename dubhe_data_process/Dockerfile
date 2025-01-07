FROM python:3.6.3

RUN pip install --no-cache-dir --user -i https://mirrors.cloud.tencent.com/pypi/simple/ --upgrade pip

RUN pip install --no-cache-dir --user -i https://mirrors.cloud.tencent.com/pypi/simple/  opencv-python-headless==4.6.0.66 \ 
    numpy==1.19.5 \
    pytest-shutil==1.7.0 \
    redis==4.3.3

COPY . /app
WORKDIR /app
