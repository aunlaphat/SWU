```sh
docker build -t lifelong-admin-eureka:0.0.3 .
docker image tag lifelong-admin-eureka:0.0.3 hubcpa.ar.co.th:5000/lifelong-admin-eureka:0.0.3
docker push hubcpa.ar.co.th:5000/lifelong-admin-eureka:0.0.3

** UAT

docker image tag lifelong-admin-eureka:0.0.3 hubcpa.swu.ac.th:30005/lifelong-admin-eureka:0.0.3
docker push hubcpa.swu.ac.th:30005/lifelong-admin-eureka:0.0.3

```

``` sh
kubectl apply -f deployment.yml
```