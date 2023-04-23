ARG MSVC_NAME=msvc-usuarios

FROM openjdk:17-jdk-alpine as builder

ARG MSVC_NAME

WORKDIR /app/$MSVC_NAME

COPY ./pom.xml /app
COPY ./Usuarios-SpringCloud/.mvn ./.mvn
COPY ./Usuarios-SpringCloud/mvnw .
COPY ./Usuarios-SpringCloud/pom.xml .

# RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot-repackage.skip && rm -r ./target/
RUN ./mvnw dependency:go-offline

COPY ./Usuarios-SpringCloud/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine

ARG MSVC_NAME

WORKDIR /app

RUN mkdir ./logs

COPY --from=builder /app/$MSVC_NAME/target/usuarios-0.0.1-SNAPSHOT.jar .

ARG PORT_APP=8001
ENV PORT $PORT_APP

EXPOSE $PORT

CMD ["java", "-jar", "usuarios-0.0.1-SNAPSHOT.jar"]