create _sources_videos folder and add mp4 (movies)

steps to run the maven app

mvn clean install -DskipTest

docker rm -f ife

docker build -t ife-linux .

docker run -d --name ife -p 8080:8080 -p 80:80 ife-linux

access your ip address to other LAN devices connected for self testing localhost/ will bring up uploaded files
