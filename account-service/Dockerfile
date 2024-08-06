# Etapa 1: Construcción
FROM gradle:7.5-jdk17 AS build

WORKDIR /app

# Copia el archivo de construcción Gradle y los archivos de origen
COPY gradle /app/gradle
COPY build.gradle /app/
COPY settings.gradle /app/
COPY src /app/src

# Ejecuta la construcción
RUN gradle clean build

# Etapa 2: Ejecutar
FROM openjdk:17-jdk-alpine

WORKDIR /app

# Copia el archivo JAR construido en la etapa de construcción
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

