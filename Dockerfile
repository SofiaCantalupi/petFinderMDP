# ---------- Etapa 1: build ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

# Primero se copia la capa de dependencias: se vuelve a copiar cuando cambia pom.xml, no en cada edicion de codigo.
COPY pom.xml ./
RUN mvn -B -ntp dependency:go-offline

# Capa de codigo fuente.
COPY src ./src
# Se saltean los tests. Como esta conectado a Spring Boot se necesita levantar una conexion con mysql, algo que no existe en el entorno de Render. Los tets romperian el build
# El nombre del file jar se construye con el build, esto es asi porque si se cambiara la version del pom.xml del proyexto y el nombre fuera fijo, se romperia la compilacion.
RUN mvn -B -ntp clean package -DskipTests \
 && cp "$(find target -maxdepth 1 -type f -name '*.jar' ! -name '*-plain.jar' | head -n 1)" /build/app.jar

# ---------- Etapa 2: runtime ----------

# La imagen que de verdad se despliega, es un JRE y Alpine. Menos tamanio de imagen ->buil y deploy mas rapido
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

RUN apk add --no-cache tzdata
ENV TZ=America/Argentina/Buenos_Aires

# Practica de seguridad: en vez de correr el contenedor como root, se crea uno spring (usuario sin privilegios)
# --from=build conecta las dos etapas: copia el app.jar (la imagen con Maven) hacia la imagen final -> el resultado final es solo el jar ya compilado
RUN addgroup -S spring && adduser -S spring -G spring
COPY --from=build --chown=spring:spring /build/app.jar /app/app.jar
USER spring

# Dimensionamiento de la memoria dinamico, la JVM usa solo el 75% del contenedor. SerialGC cambia el GargabeCollector que viene por default, es mas adecuado a GPU bajas de REnder
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+UseSerialGC"

# Documentacion nada mas
# Render inyecta la variable de entorno $PORT en runtime y la app tiene que escuchar en ese puerto
EXPOSE 8080

# `exec` so the JVM is PID 1 and receives Render's SIGTERM for graceful shutdown.
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
