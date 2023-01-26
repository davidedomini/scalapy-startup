FROM ubuntu:latest
RUN apt-get update;
RUN apt install -y default-jdk
RUN apt install -y python3
RUN apt-get -y install python3-pip
RUN pip3 install numpy
COPY ./ $HOME/scalapy-startup/
WORKDIR $HOME/scalapy-startup/
RUN ./gradlew
CMD ./gradlew --console=plain runTryNumpy