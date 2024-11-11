# Utiliser une image Java officielle comme base
FROM openjdk:17-jdk-slim

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier .jar généré par Maven depuis le répertoire target vers le conteneur
COPY target/Foyer-0.0.1-SNAPSHOT.jar foyer-app.jar

# Exposer le port de l'application Spring Boot
EXPOSE 7010

# Démarrer l'application sans spécifier le port dans la ligne de commande, il sera configuré via application.properties
ENTRYPOINT ["java", "-jar", "foyer-app.jar"]
