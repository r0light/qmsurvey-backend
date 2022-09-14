# Quality Model Validation Survey frontend

A web application that acts as the backend for conducting quality model validation surveys with the following application: https://github.com/r0light/qmsurvey-frontend

## Run in development mode

```shell
./gradlew bootRun
```

or

```shell
./gradlew bootRun --args='--spring.profiles.active=dev'
```

The application will be available at `localhost:8080/backend/admin`.

To properly shutdown the application while using `bootRun`, type `stop` in the console and hit `Enter`.

## Run in production

Start a mongoDB database with docker, for example:

```shell
docker run --name mongodb -d -p 27017:27017 --rm --env MONGO_INITDB_ROOT_USERNAME=dataManager --env MONGO_INITDB_ROOT_PASSWORD=69kxLBsK60WX8lH3DMgY --env MONGO_INITDB_DATABASE=qmsurvey mongo:latest
```

```shell
./gradlew bootRun --args='--spring.profiles.active=prod'
```

Or you can build the application and run the respective `.jar` file:

```shell
./gradlew bootJar
java -jar "build/libs/qmsurvey-0.0.1.jar" --spring.profiles.active=prod
```

The application will be available at `localhost:8080/backend/admin`.

## Build as Docker image

```shell
./gradlew bootBuildImage
```

```shell
docker run -it -p 8080:8080 --rm --name qmsurvey-backend --link mongodb:mongodb \
    --env SPRING_PROFILES_ACTIVE=prod \
    --env QMSURVEY_ADMIN_EMAIL=robin@test.de \
    --env QMSURVEY_ADMIN_PASSWORD=12344321 \
    --env QMSURVEY_DATA_MONGODB_HOST=mongodb \
    --env QMSURVEY_DATA_MONGODB_PORT=27017 \
    --env QMSURVEY_DATA_MONGODB_DATABASE=qmsurvey \
    --env QMSURVEY_DATA_MONGODB_USERNAME=dataManager \
    --env QMSURVEY_DATA_MONGODB_PASSWORD=69kxLBsK60WX8lH3DMgY \
     rlight/qmsurvey:latest
```

