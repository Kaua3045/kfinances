FROM openjdk:19

WORKDIR /kfinances/api/

COPY build/libs/com.kaua.finances-0.0.1-SNAPSHOT.jar /kfinances/api/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "./com.kaua.finances-0.0.1-SNAPSHOT.jar"]