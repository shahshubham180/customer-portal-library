FROM openjdk:11
EXPOSE 8080
ADD target/customer-portal-library-0.0.1-SNAPSHOT.jar customer-portal-library-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/customer-portal-library-0.0.1-SNAPSHOT.jar"]