```sh
docker build -t lifelong-operation-api:0.0.1-beta.2-alpha.17 . &&
docker image tag lifelong-operation-api:0.0.1-beta.2-alpha.17 hubcpa.ar.co.th:5000/lifelong-operation-api:0.0.1-beta.2-alpha.17 &&
docker push hubcpa.ar.co.th:5000/lifelong-operation-api:0.0.1-beta.2-alpha.17

** UAT
docker build -t lifelong-operation-api:0.0.1-beta.2-alpha.17 . &&
docker image tag lifelong-operation-api:0.0.1-beta.2-alpha.17 hubcpa.swu.ac.th/lifelong-operation-api:0.0.1-beta.2-alpha.17 &&
docker push hubcpa.swu.ac.th/lifelong-operation-api:0.0.1-beta.2-alpha.17

```

``` sh
kubectl apply -f deployment.yml
```