FROM openjdk:17-jdk-alpine as builder

WORKDIR /app/msvc-usuarios

COPY ./pom.xml /app
COPY ./Usuarios-SpringCloud/.mvn ./.mvn
COPY ./Usuarios-SpringCloud/mvnw .
COPY ./Usuarios-SpringCloud/pom.xml .

# RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot-repackage.skip && rm -r ./target/
RUN ./mvnw dependency:go-offline

COPY ./Usuarios-SpringCloud/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app

RUN mkdir ./logs

COPY --from=builder /app/msvc-usuarios/target/usuarios-0.0.1-SNAPSHOT.jar .

EXPOSE 8001

CMD ["java", "-jar", "usuarios-0.0.1-SNAPSHOT.jar"]