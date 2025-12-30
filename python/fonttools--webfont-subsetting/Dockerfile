FROM python:3.12-alpine
RUN apk add --no-cache --virtual .build-deps gcc g++
RUN pip install brotli
RUN pip install fonttools
RUN pip install rich
RUN apk del .build-deps
WORKDIR /app
CMD ["python3", "run.py"]
