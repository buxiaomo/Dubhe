# docker build -t buxiaomo/notebook:12.4.1-cudnn-devel-ubuntu20.04 .
FROM nvidia/cuda:12.4.1-cudnn-devel-ubuntu20.04

RUN apt-get update \
    && apt-get install --no-install-recommends vim git curl -y \
    && curl https://repo.anaconda.com/miniconda/Miniconda3-py310_24.9.2-0-Linux-x86_64.sh -o /tmp/miniconda3.sh \
    && chmod +x /tmp/miniconda3.sh \
    && /tmp/miniconda3.sh -b -p /opt/conda \
    && rm /tmp/miniconda3.sh \
    && touch /opt/conda/bin/activate \
    && echo 'export PATH="/opt/conda/bin:$PATH"' >> /opt/conda/bin/activate \
    && echo 'export PATH="/opt/conda/bin:$PATH"' >> /etc/profile \
    && /opt/conda/bin/activate \
    && /opt/conda/bin/conda clean --all -y \
    && /opt/conda/bin/conda init bash \
    && apt-get clean all \
    && rm -rf /var/lib/apt/lists/*

ENV PATH=/opt/conda/bin:$PATH
RUN conda install jupyter -y --quiet \
    && conda clean --all -y
RUN ln -sf /bin/bash /bin/sh
WORKDIR /workspace
CMD [ "jupyter-lab", "--ip=0.0.0.0", "--port=8888", "--no-browser", "--allow-root" ]