# Étape 1 : Construction du .jar en local
FROM maven:3.8.1-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests -e

# Vérifiez le contenu de /app/target pour voir si le fichier .jar est bien là
RUN ls -l /app/target

# Étape 2 : Utiliser l’image Java légère pour exécuter l’application
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copier le fichier .jar depuis l'étape de build
COPY --from=build /app/target/foyer-0.0.1-SNAPSHOT.jar foyer-app.jar
EXPOSE 8110
ENTRYPOINT ["java", "-jar", "foyer-app.jar"]
