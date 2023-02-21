FROM openjdk:11
EXPOSE 8080
ADD target/customer-portal-library.jar customer-portal-library.jar
ENTRYPOINT ["java","-jar","/customer-portal-library.jar"]