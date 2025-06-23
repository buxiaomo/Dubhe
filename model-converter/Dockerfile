FROM tensorflow/tensorflow:2.10.0

WORKDIR /app
RUN pip install web.py tf2onnx
COPY . /app

ENTRYPOINT ["python3", "main.py"]
