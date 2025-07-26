FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/order-service-1.0.jar order-service-1.0.jar
ENTRYPOINT ["java", "-jar", "order-service-1.0.jar"]