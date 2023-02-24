FROM openjdk:11
EXPOSE 8081
ADD target/customer-portal-library-0.0.1-SNAPSHOT.jar customer-portal-library-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/customer-portal-library-0.0.1-SNAPSHOT.jar"]