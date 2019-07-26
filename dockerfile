FROM openjdk:12
WORKDIR /home/ManBot
COPY ./target/ManBot-1.0-SNAPSHOT-jar-with-dependencies.jar /
ENTRYPOINT ["java","-jar","ManBot-1.0-SNAPSHOT-jar-with-dependencies.jar"]