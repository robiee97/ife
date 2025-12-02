#!/bin/bash

echo "Starting HLS conversion..."

for vid in $(ls /opt/source_videos/*.mp4 | xargs -n 1 basename | sed 's/\.mp4//')
do
  mkdir -p /opt/videos/$vid
  ffmpeg -i /opt/source_videos/$vid.mp4 \
    -codec: copy \
    -start_number 0 \
    -hls_time 10 \
    -hls_list_size 0 \
    -f hls /opt/videos/$vid/index.m3u8

  # Generate thumbnail at 5 seconds
  ffmpeg -i /opt/source_videos/$vid.mp4 -ss 01:00:00 -vframes 1 /opt/videos/$vid/thumb.jpg
done

echo "HLS conversion DONE"

echo "Starting Spring Boot..."
java -jar /opt/app/app.jar &

echo "Starting Nginx..."
nginx -g "daemon off;"
