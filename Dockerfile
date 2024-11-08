# Étape 1 : Construire l'application avec Maven
FROM maven:3.8.1-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

# Étape 2 : Utiliser une image Java légère pour exécuter l’application
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copier le fichier .jar depuis l'étape de build
COPY --from=build /app/target/foyer-0.0.1-SNAPSHOT.jar foyer-app.jar
EXPOSE 8110
ENTRYPOINT ["java", "-jar", "foyer-app.jar"]
