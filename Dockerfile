FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk nginx ffmpeg ca-certificates && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

RUN mkdir -p /opt/app /opt/videos /opt/source_videos

WORKDIR /opt/app

COPY target/*.jar /opt/app/app.jar
COPY nginx.conf /etc/nginx/nginx.conf
COPY _sources_videos/ /opt/source_videos/
COPY start.sh /opt/start.sh

RUN chmod +x /opt/start.sh

EXPOSE 80

CMD ["/opt/start.sh"]
