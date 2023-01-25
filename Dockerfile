FROM alpine:latest
RUN apk update; apk add openjdk11
# Install python/pip
ENV PYTHONUNBUFFERED=1
RUN apk add --update --no-cache python3 && ln -sf python3 /usr/bin/python
RUN python3 -m ensurepip
RUN pip3 install --no-cache --upgrade pip setuptools
COPY ./ $HOME/scalapy-startup/
WORKDIR $HOME/scalapy-startup/
RUN ./gradlew
CMD ./gradlew --console=plain runTryNumpy
