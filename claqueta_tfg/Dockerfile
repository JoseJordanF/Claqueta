# Etapa de construcción de la API
FROM gradle:7-jdk11 AS build
WORKDIR /home/gradle/src
COPY . .
RUN gradle buildFatJar --no-daemon

# Etapa de ejecución y configuración de Nginx
FROM nginx:alpine
WORKDIR /app

# Copiar el archivo JAR desde la etapa de construcción
COPY --from=build /home/gradle/src/build/libs/*.jar /app/claqueta.jar

# Copiar la configuración de Nginx y los archivos estáticos
COPY nginx.conf /etc/nginx/nginx.conf
COPY mime.types /etc/nginx/mime.types
COPY cliente-claqueta_tfg /usr/share/nginx/html

# Instalar Java en el contenedor
RUN apk add --no-cache openjdk11-jre

# Ejecutar la API y Nginx
CMD ["sh", "-c", "java -jar /app/claqueta.jar & nginx -g 'daemon off;'"]
