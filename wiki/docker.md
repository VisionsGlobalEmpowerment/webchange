## Working with docker

###Production
Curently we use docker for voice recognition instances.

To build new image for production use command:
```shell script
$ docker image build -t webchange-voice-recognition  -f ./docker/voice-recognition/Dockerfile .
```
Tag image:
```shell script
$ docker tag webchange-voice-recognition webchange/webchange-voice-recognition:latest
```
If you wish to update image after building use:
```shell script
$ docker push webchange/webchange-voice-recognition:latest
```
"latest" is version or tag name.

If you need just to use existing image, pull it from repository:
```shell script
$ docker pull webchange/webchange-voice-recognition:latest
```
To run image:
```shell script
$ docker run  webchange/webchange-voice-recognition:latest
```

### Development

The same actions may be executed with image prepared for dev purposes.
With some changes. Use webchange-voice-recognition-dev instead of webchange-voice-recognition. 
To run image use:
```
$ docker run --mount type=bind,source="$(pwd)",target=/usr/src/app webchange-voice-recognition-dev
```
Here mount option will bind you local directory instead of instance app folder, so all your changes will be applied immediately

You can even mount maven cache to the image...
```
$ docker run \
 --mount type=bind,source="$(pwd)",target=/usr/src/app \
 --mount type=bind,source="$HOME/.m2",target=/root/.m2 \
 webchange/webchange-voice-recognition-dev
```



### Usefull commands
List of containers:
```shell script
 $ docker container ps
```
Remove all stopped containers:
```shell script
 $ docker container prune
```
Stop container:
```shell script
 $ docker stop <CONTAINER ID>
```
List of local images: 
```shell script
 $ docker image ls
```
Remove image:
```shell script
 $ docker rmi <Image id>
```



### Build new docker image


```
lein clean
lein uberjar
```

```
sudo docker image build -t webchange-voice-recognition-dev -f ./docker/voice-recognition-dev/Dockerfile .
```

```
$ docker run --mount type=bind,source="$(pwd)",target=/usr/src/app webchange-voice-recognition-dev
```
