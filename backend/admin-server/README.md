```sh
docker build -t lifelong-admin-sv:0.0.1 .
docker image tag lifelong-admin-sv:0.0.1 hubcpa.ar.co.th:5000/lifelong-admin-sv:0.0.1
docker push hubcpa.ar.co.th:5000/lifelong-admin-sv:0.0.1

** UAT

docker image tag lifelong-admin-sv:0.0.1 hubcpa.swu.ac.th:30005/lifelong-admin-sv:0.0.1
docker push hubcpa.swu.ac.th:30005/lifelong-admin-sv:0.0.1

```

``` sh
kubectl apply -f deployment.yml
```