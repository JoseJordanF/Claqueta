# Etapa de construcción de la API
FROM gradle:7-jdk11 AS build
WORKDIR /home/gradle/src
COPY . .
RUN gradle buildFatJar --no-daemon

# Etapa de ejecución de la API
FROM openjdk:11-jre-slim AS runtime
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/claqueta.jar

# Nginx para servir archivos estáticos y como proxy
FROM nginx:alpine
COPY --from=runtime /app/claqueta.jar /app/claqueta.jar
COPY nginx.conf /etc/nginx/nginx.conf
COPY mime.types /etc/nginx/mime.types
COPY web /usr/share/nginx/html

# Instalar Java en el contenedor de Nginx
RUN apk add --no-cache openjdk11-jre

# Ejecuta la API y Nginx
CMD ["sh", "-c", "java -jar /app/claqueta.jar & nginx -g 'daemon off;'"]
