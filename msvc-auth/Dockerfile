ARG MSVC_NAME=msvc-auth

FROM openjdk:17-jdk-alpine as builder

ARG MSVC_NAME

WORKDIR /app/$MSVC_NAME

COPY ./pom.xml /app
COPY ./msvc-auth/.mvn ./.mvn
COPY ./msvc-auth/mvnw .
COPY ./msvc-auth/pom.xml .

# RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot-repackage.skip && rm -r ./target/
RUN ./mvnw dependency:go-offline

COPY ./msvc-auth/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine

ARG MSVC_NAME

WORKDIR /app

RUN mkdir ./logs

COPY --from=builder /app/$MSVC_NAME/target/auth-0.0.1-SNAPSHOT.jar .

ARG PORT_APP=9000
ENV PORT $PORT_APP

EXPOSE $PORT

CMD ["java", "-jar", "auth-0.0.1-SNAPSHOT.jar"]