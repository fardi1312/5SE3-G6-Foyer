# Utiliser une image Java officielle comme base
FROM openjdk:17-jdk-slim

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier .jar généré par Maven depuis le répertoire target vers le conteneur
COPY target/Foyer-0.0.1-SNAPSHOT.jar foyer-app.jar

# Exposer le nouveau port de l'application Spring Boot
EXPOSE 8082

# Démarrer l'application sur le port 8082
ENTRYPOINT ["java", "-jar", "foyer-app.jar", "--server.port=8082"]
