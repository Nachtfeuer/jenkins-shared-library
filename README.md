# jenkins-shared-pipeline
Useful functionality for a Jenkins shared pipeline

[![Build Status](https://travis-ci.org/Nachtfeuer/jenkins-shared-library.svg?branch=master)](https://travis-ci.org/Nachtfeuer/jenkins-shared-library)
[![Coverage Status](https://coveralls.io/repos/github/Nachtfeuer/jenkins-shared-library/badge.svg?branch=master)](https://coveralls.io/github/Nachtfeuer/jenkins-shared-library?branch=master)
[![GitHub license](https://img.shields.io/github/license/Nachtfeuer/jenkins-shared-library.svg)](https://github.com/Nachtfeuer/jenkins-shared-library/blob/master/LICENSE)
[![Documentation](https://img.shields.io/badge/documentation-ok-brightgreen.svg)](https://nachtfeuer.github.io/jenkins-shared-library/)
[![CodeFactor](https://www.codefactor.io/repository/github/nachtfeuer/jenkins-shared-library/badge)](https://www.codefactor.io/repository/github/nachtfeuer/jenkins-shared-library)
[![BCH compliance](https://bettercodehub.com/edge/badge/Nachtfeuer/jenkins-shared-library?branch=master)](https://bettercodehub.com/)

## Build the project

```bash
./gradlew
```

## Build with Docker

```bash
docker build -t openjdk-1.8:1.0 .
docker run --rm -v $PWD:/mnt/host -w /mnt/host -u $(id -u):$(id -g) -it openjdk-1.8:1.0 bash -c "./gradlew"
```

## How to run your own Jenkins

- it's important to mount jenkins home to keep installation also the Docker container goes away
- Use `--rm` to automatically remove the container. If the container is gone simply run again.
- In my setup the initial admin password an be found at `cat /work/docker/jenkins/secrets/initialAdminPassword`

```bash
mkdir -p /work/docker/jenkins
docker run --rm --name=jenkins -v /work/docker/jenkins:/var/jenkins_home -p 8080:8080 -d jenkinsci/blueocean
```

 - visit that Jenkins on your `box`  at http://localhost:8080

## Useful links

 - https://hub.docker.com/r/jenkinsci/blueocean/
 - https://jenkins.io/doc/book/pipeline/syntax
 - https://jenkins.io/doc/book/pipeline/syntax/#declarative-pipeline
 - https://jenkins.io/doc/book/pipeline/docker/
 - https://github.com/kt3k/coveralls-gradle-plugin
 - https://www.mkdocs.org
