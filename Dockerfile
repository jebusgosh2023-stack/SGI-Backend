# Usa una imagen base con Maven y Java 17 para compilar
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Ejecuta la compilación omitiendo los tests
RUN mvn clean package -DskipTests

# Etapa final: Usa solo el JRE para que la imagen sea ligera
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Copia solo el archivo jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
