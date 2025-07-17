FROM nvidia/cuda:11.2.2-cudnn8-devel-ubuntu18.04
WORKDIR /workspace
RUN chmod 777 /tmp
RUN apt-key adv --recv-keys --keyserver keyserver.ubuntu.com A4B469963BF863CC
# 删除nvidia更新源以避免apt update失败，安装python3-pip会同时安装python3
RUN rm -rf /etc/apt/sources.list.d && apt update && apt install -y python3-pip
# 建立python软连接到python3，以兼容使用python运行命令的情况
RUN ln -s /usr/bin/python3 /usr/bin/python
# 升级pip3以支持manylinux的方式安装wheel包
RUN pip3 install --upgrade pip  -i https://pypi.douban.com/simple/
RUN pip install tensorflow-gpu==2.1.0 -i https://pypi.douban.com/simple/
RUN python3 -m pip install  -i https://pypi.tuna.tsinghua.edu.cn/simple --find-links https://oneflow-static.oss-cn-beijing.aliyuncs.com/staging/dev_serving/pip.index.html oneflow_cu102
RUN pip install torch==1.6.0 torchvision==0.7.0 -i https://mirrors.aliyun.com/pypi/simple/
RUN pip install fastapi -i https://mirrors.aliyun.com/pypi/simple/
RUN pip install python-multipart -i https://mirrors.aliyun.com/pypi/simple/
RUN pip install uvicorn  -i https://mirrors.aliyun.com/pypi/simple/
RUN pip install numpy -i https://mirrors.aliyun.com/pypi/simple/