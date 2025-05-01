# Etapa 1: Build opcional (si usas Maven dentro del contenedor)
# Puedes omitir esta parte si ya tienes el .jar generado localmente

# FROM maven:3.9-eclipse-temurin-21 AS build
# WORKDIR /app
# COPY . .
# RUN mvn clean package -DskipTests

# Etapa 2: Imagen ligera con JDK 21 para ejecutar el jar
FROM eclipse-temurin:21-jdk as runtime

# Directorio donde irá la app
WORKDIR /app

# Copiar el JAR desde el build (o desde local si no usas etapa de build)
COPY target/nomProyecto-0.0.1-SNAPSHOT.jar server.jar

# Exponer el puerto que usa Spring Boot (por defecto 8080)
EXPOSE 8080

# Comando para ejecutar el JAR
ENTRYPOINT ["java", "-jar", "server.jar"]
