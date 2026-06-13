# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copiar configuración de dependencias del proyecto multimodular
COPY pom.xml .
COPY bff-gateway/pom.xml bff-gateway/
COPY ms-inventario/pom.xml ms-inventario/
COPY ms-pedidos/pom.xml ms-pedidos/
COPY ms-envios/pom.xml ms-envios/

# Copiar el código fuente del módulo específico
COPY ms-pedidos/src ms-pedidos/src/

# Compilar solo el módulo de ms-pedidos sin ejecutar pruebas
RUN mvn clean package -pl ms-pedidos -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/ms-pedidos/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
