FROM nvidia/cuda:12.4.1-cudnn-devel-ubuntu20.04

COPY sources.list /etc/apt/sources.list

RUN rm -rf /etc/apt/sources.list.d/* \
    && apt-get update \
    && apt-get upgrade -y \
    && rm -rf /var/lib/apt/lists/*

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

ENV PIP_NO_REQUIRE_HASHES=1
COPY . /model_measuring

RUN if [ `uname -m` = "x86_64" ] && [ `uname -o` = "GNU/Linux" ]; then \
        conda env create --platform linux-64 --file=/model_measuring/pytorch.yaml --yes; \
    else \
        conda env create --platform linux-aarch64 --file=/model_measuring/pytorch.yaml --yes; \
    fi \
    && /opt/conda/bin/conda clean --all -y && rm -rf /root/.cache/* \
    && bash -c "source /opt/conda/bin/activate pytorch"

CMD ["conda", "run", "-n", "pytorch", "python", "/model_measuring/app.py"]