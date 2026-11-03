# syntax=docker/dockerfile:1

# ============================================================
# Estágio 1 - BUILD (Maven + JDK 21)
# ============================================================
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

# Copia só o pom.xml primeiro: o Docker reaproveita o cache das
# dependências enquanto o pom.xml não mudar (builds bem mais rápidos).
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -q dependency:go-offline

# Agora o código-fonte e o empacotamento.
# -DskipTests: os testes do projeto precisam do Oracle (DB_URL etc.),
# que não existe durante o build da imagem.
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -q clean package -DskipTests

# ============================================================
# Estágio 2 - RUNTIME (somente JRE 21, imagem enxuta)
# ============================================================
FROM eclipse-temurin:21-jre-alpine AS runtime

# Usuário sem privilégios (não roda como root)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app
COPY --from=build /build/target/ecoenergia-*.jar app.jar

EXPOSE 8080

# Ajustes de JVM para container (podem ser sobrescritos no compose)
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseContainerSupport"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]