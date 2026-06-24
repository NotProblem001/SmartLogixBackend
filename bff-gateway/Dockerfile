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
COPY bff-gateway/src bff-gateway/src/

# Compilar solo el módulo de BFF Gateway sin ejecutar pruebas
RUN mvn clean package -pl bff-gateway -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/bff-gateway/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xms256m", "-Xmx256m", "-jar", "app.jar"]
