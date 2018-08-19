# jenkins-shared-pipeline
Useful functionality for a Jenkins shared pipeline

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
