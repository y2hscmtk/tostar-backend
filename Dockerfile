FROM openjdk:17
COPY ./build/libs/tostar-0.0.1-SNAPSHOT.jar tostar.jar
ENTRYPOINT ["java", "-jar", "tostar.jar"]