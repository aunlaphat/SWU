```sh
docker build -t lifelong-backend:0.0.1-beta.2-alpha.17 .
docker image tag lifelong-backend:0.0.1-beta.2-alpha.17 hubcpa.ar.co.th:5000/lifelong-backend:0.0.1-beta.2-alpha.17
docker push hubcpa.ar.co.th:5000/lifelong-backend:0.0.1-beta.2-alpha.17

for uat
docker build -t lifelong-backend:0.0.1-beta.2-alpha.17 . &&
docker image tag lifelong-backend:0.0.1-beta.2-alpha.17 hubcpa.swu.ac.th/lifelong-backend:0.0.1-beta.2-alpha.17  &&
docker push hubcpa.swu.ac.th/lifelong-backend:0.0.1-beta.2-alpha.17
```

``` sh
kubectl apply -f deployment.yml
```