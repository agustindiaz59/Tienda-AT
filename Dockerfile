# Etapa 2: Imagen ligera con JDK 21 para ejecutar el jar
FROM bellsoft/liberica-openjre-alpine

# Directorio donde irá la app
WORKDIR /app

# Copiar el JAR desde el build (o desde local si no usas etapa de build)
COPY target/animatuc-backend-1.0.jar server.jar

# Exponer el puerto que usa Spring Boot (por defecto 8080)
EXPOSE 8080

# Comando para ejecutar el JAR
ENTRYPOINT ["java", "-jar", "server.jar"]
