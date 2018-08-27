FROM centos:7

LABEL version="1.1"
LABEL description="build image for gradlew and Groovy"
LABEL maintainer="Thomas Lehmann <thomas.lehmann.private@gmail.com"

RUN yum -y install yum -y install java-1.8.0-openjdk-devel git
RUN mkdir -p /usr/java
RUN ln -s $(dirname $(dirname $(find /usr -name "javac"|grep openjdk))) /usr/java/default
ENV JAVA_HOME=/usr/java/default
