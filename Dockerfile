FROM --platform=linux/amd64 ubuntu:latest

# INITILISATION DE L IMAGE

ENV DEBIAN_FRONTEND=noninteractive

RUN apt update -y && apt upgrade -y && apt install gcc openjdk-21-jdk -y

ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# TWISK

WORKDIR /app

COPY out/artifacts/twisk_jar/twisk.jar /app/twisk.jar

CMD ["java", "-jar", "/app/twisk.jar"]