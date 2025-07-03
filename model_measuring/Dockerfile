FROM nvidia/cuda:12.4.1-cudnn-devel-ubuntu20.04

RUN apt-get update \
    && apt-get install --no-install-recommends vim git curl -y \
    && curl https://repo.anaconda.com/miniconda/Miniconda3-py310_24.9.2-0-Linux-$(uname -m).sh -o /tmp/miniconda3.sh \
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
COPY . /model_measuring
RUN conda env create --file=/model_measuring/pytorch.yaml --yes \
    && /opt/conda/bin/conda clean --all -y && rm -rf /root/.cache/*

# RUN conda activate pytorch

# CMD [ "python", "/model_measuring/app.py" ]