# 스프링클라우드 + 마이크로서비스아키텍쳐

## spring boot + JPA + H2 + spring-cloud-starter-netflix-eureka-client
  1. user-service + spring-cloud-starter-config + jwt
   <br/>1-0. spring-boot-starter-security
    <br/>1-1. spring-boot-starter-actuator
    <br/>1-2. spring-cloud-starter-bus-amqp
    <br/>1-3. spring-cloud-starter-sleuth
    <br/>1-4. spring-cloud-starter-zipkin
    <br/>1-5. micrometer-registry-prometheus
    <br/>1-6. spring-cloud-starter-config
  2. order-servcie
    <br/>2-1. spring-boot-starter-actuator
    <br/>2-2. spring-cloud-starter-bus-amqp
    <br/>2-3. spring-cloud-starter-sleuth
    <br/>2-4. spring-cloud-starter-zipkin
    <br/>2-5. micrometer-registry-prometheus
    <br/>2-6. spring-cloud-starter-config
  3. catalog-service 
  
  
 ## discovery-server   
  1. spring-cloud-starter-netflix-eureka-server
  
  
 ## apigateway-service + jwt + filter
  1. spring-cloud-starter-gateway
  2. spring-cloud-starter-netflix-eureka-clien
  3. spring-cloud-starter-config
  4. spring-boot-starter-actuator
  5. spring-cloud-starter-config
  6. micrometer-registry-prometheus


 ## config-cloud-service
  1. spring-cloud-config-server
  2. spring-boot-starter-actuator
  3. spring-boot-starter-actuator
  4. spring-cloud-starter-bus-amqp
