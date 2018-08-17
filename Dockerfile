FROM centos:7
RUN yum -y install yum -y install java-1.8.0-openjdk-devel
RUN mkdir -p /usr/java
RUN ln -s $(dirname $(dirname $(find /usr -name "javac"|grep openjdk))) /usr/java/default
ENV JAVA_HOME=/usr/java/default