FROM centos:7

LABEL version="1.2"
LABEL description="build image for openjdk 8"
LABEL maintainer="Thomas Lehmann <thomas.lehmann.private@gmail.com"

RUN yum -y install yum -y install java-1.8.0-openjdk-devel git python-virtualenv python-setuptools
RUN easy_install pip
RUN mkdir -p /usr/java
RUN ln -s $(dirname $(dirname $(find /usr -name "javac"|grep openjdk))) /usr/java/default
ENV JAVA_HOME=/usr/java/default
