# Etapa 1: build da aplicação
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app
COPY src .

# Build com Maven
RUN ./mvnw package -DskipTests

# Etapa 2: imagem mais leve pra rodar a aplicação
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copia o jar do build anterior
COPY --from=builder /app/target/*.jar app.jar

# Porta usada pelo Render (pegando da env PORT)
ENV PORT=8080
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
