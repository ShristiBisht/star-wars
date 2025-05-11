FROM openjdk:17-jdk-slim
WORKDIR /app
COPY . .
RUN ./mvnw clean package 
EXPOSE 8080
RUN cp target/*.jar starWars.jar
CMD ["java", "-jar", "starWars.jar"]