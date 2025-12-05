FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive

# Install Java, NGINX, FFmpeg
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk nginx ffmpeg ca-certificates && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Create required app directories
RUN mkdir -p \
    /opt/app \
    /opt/app/_sources_videos \
    /opt/app/_hls_videos \
    /opt/app/_thumbnails \
    /opt/app/static/ife-frontend && \
    chmod -R 777 /opt/app/_sources_videos /opt/app/_hls_videos /opt/app/_thumbnails

WORKDIR /opt/app

# Copy JAR
COPY target/*.jar /opt/app/app.jar

# Copy nginx config
COPY nginx.conf /etc/nginx/nginx.conf

# Copy source videos (if any exist)
COPY _sources_videos/ /opt/app/_sources_videos/

# Copy frontend build
COPY src/main/resources/static/ife-frontend/ /opt/app/static/ife-frontend/

# Expose HTTP port
EXPOSE 80

# Start NGINX + Spring Boot
CMD service nginx start && java -jar /opt/app/app.jar
