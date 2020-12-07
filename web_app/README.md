## To start the application locally
```
npm install
```  
```
bash conf_gen.sh 8080 127.0.0.1
```  
```
npm run heroku-postbuild
```  
```
npm run start
```  
Then go to localhost:8080 in your browser

## To run with docker
```
docker build --build-arg PORT=<port> --build-arg IP=<ip> -t <img_name> <URL_to_git>#<branch> 
```  
```
docker run --publish <port>:<port> --name <container_name> <img_name>
```  
- <ip>: the ip/domain url for user to access the web app. Make sure it is the public ip or domain name of the machine running the docker container
- <port>: the port which the web service is run. Make sure all <port> replaced are the same value
- <img_name>: give a name to web_app docker image. Make sure it is consistent for both occurance above.
- <container_name>: give a name to the container running the web_app docker image
#### Alternatively to allow configuration to a specific mongodb during build:  
```
docker build --build-arg PORT=<port> --build-arg IP=<ip> --build-arg MONGODB_URI=<mongo> -t <img_name> <URL_to_git>#<branch>
```  
#### Alternatively for a quick setup:
```
bash docker_img_build.sh <port> <ip> <URL_to_git>#<branch>
```