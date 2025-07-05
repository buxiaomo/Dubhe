FROM python:3.7.17

RUN apt-get update \
    && apt-get upgrade -y \
    && rm -rf /var/lib/apt/lists/*

RUN apt-get update \
    && apt-get install --no-install-recommends vim git curl -y \
    && curl https://repo.anaconda.com/archive/Anaconda3-2020.11-Linux-$(uname -m).sh  -o /tmp/anaconda3.sh \
    && chmod +x /tmp/anaconda3.sh \
    && /tmp/anaconda3.sh -b -p /opt/conda \
    && rm /tmp/anaconda3.sh \
    && touch /opt/conda/bin/activate \
    && echo 'export PATH="/opt/conda/bin:$PATH"' >> /opt/conda/bin/activate \
    && echo 'export PATH="/opt/conda/bin:$PATH"' >> /etc/profile \
    && /opt/conda/bin/activate \
    && /opt/conda/bin/conda clean --all -y \
    && /opt/conda/bin/conda init bash \
    && apt-get clean all \
    && rm -rf /var/lib/apt/lists/*

ENV PIP_NO_REQUIRE_HASHES=1
ENV PATH=/opt/conda/bin:$PATH
COPY . /dubhe-visual-server
RUN conda config --add channels https://mirrors.ustc.edu.cn/anaconda/pkgs/main/ \
    && conda config --set show_channel_urls yes \
    && conda env create --file=/dubhe-visual-server/dubhe_visual.yaml --yes \
    && /opt/conda/bin/conda clean --all -y && rm -rf /root/.cache/* \
    && bash -c "source /opt/conda/bin/activate visual"