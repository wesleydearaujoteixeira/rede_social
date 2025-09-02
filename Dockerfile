# Etapa 1: build da aplicação
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copia todos os arquivos, incluindo mvnw e pom.xml
COPY redeSocial .

# Garantir permissão de execução para o mvnw
RUN chmod +x ./mvnw

# Build com Maven
RUN ./mvnw package -DskipTests

# Etapa 2: imagem mais leve pra rodar a aplicação
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copia o jar do build anterior
COPY --from=builder /app/target/*.jar app.jar

# Porta usada pelo Render (pegando da env PORT)
EXPOSE 8080

# Rodar a aplicação
CMD ["java", "-jar", "app.jar"]
