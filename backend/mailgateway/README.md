```sh
docker build -t lifelong-emailgateway:0.0.1-beta.1-alpha.29 .
docker image tag lifelong-emailgateway:0.0.1-beta.1-alpha.29 hubcpa.ar.co.th:5000/lifelong-emailgateway:0.0.1-beta.1-alpha.29
docker push hubcpa.ar.co.th:5000/lifelong-emailgateway:0.0.1-beta.1-alpha.29

for uat
docker image tag lifelong-emailgateway:0.0.1-beta.1-alpha.29 hubcpa.swu.ac.th:30005/lifelong-emailgateway:0.0.1-beta.1-alpha.29
docker push hubcpa.swu.ac.th:30005/lifelong-emailgateway:0.0.1-beta.1-alpha.29
```

``` sh
kubectl apply -f deployment.yml
```

