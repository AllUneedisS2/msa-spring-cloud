* Spring Boot 3.2.x (3.2.2, 3.2.4, 3.2.7) + Kubernetes 배포 (spring3.2_k8s 브랜치)
* user-service, order-service, catalog-service를 Kubernetes에 배포
* docker-compose.yml 파일을 이용하여 docker kafka를 기동
  * docker-compose.yml 파일에서 'KAFKA_ADVERTISED_LISTENERS' 설정에서 사용하는 IP로 변경
* user-service 실행
  * kubectl apply -f ./k8s/user-deploy.yml
  * PORT: 60000, NodePort: 32000 사용
* order-service 실행
  * kubectl apply -f ./k8s/order-deploy.yml
  * PORT: 10000, NodePort: 31000 사용
* catalog-service 실행
  * kubectl apply -f ./k8s/catalog-deploy.yml
  * PORT: 8080, NodePort: Random 사용
