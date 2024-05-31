```sh
docker build -t lifelong-webhook-api:0.0.1-beta.2-alpha.17 .
docker image tag lifelong-webhook-api:0.0.1-beta.2-alpha.17 hubcpa.ar.co.th:5000/lifelong-webhook-api:0.0.1-beta.2-alpha.17
docker push hubcpa.ar.co.th:5000/lifelong-webhook-api:0.0.1-beta.2-alpha.17

** UAT
docker build -t lifelong-webhook-api:0.0.1-beta.2-alpha.17 . &&
docker image tag lifelong-webhook-api:0.0.1-beta.2-alpha.17 hubcpa.swu.ac.th/lifelong-webhook-api:0.0.1-beta.2-alpha.17  &&
docker push hubcpa.swu.ac.th/lifelong-webhook-api:0.0.1-beta.2-alpha.17

```

``` sh
kubectl apply -f deployment.yml

UAT

kubectl apply -f deployment-uat.yml
```