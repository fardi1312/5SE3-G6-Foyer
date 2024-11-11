FROM openjdk:17
EXPOSE 8087
ADD target/Foyer-0.0.1.jar Foyer-0.0.1.jar
ENTRYPOINT ["java","-jar","/Foyer-0.0.1.jar"]