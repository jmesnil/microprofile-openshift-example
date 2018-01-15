FROM jboss/base-jdk:8

ADD target/numbers-swarm.jar /opt/numbers-swarm.jar
RUN touch audit.log

EXPOSE 8080
ENTRYPOINT ["java", "-Djava.net.preferIPv4Stack=true", "-jar", "/opt/numbers-swarm.jar"]