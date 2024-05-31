# SwuLifelongUi

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 16.0.0.

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via a platform of your choice. To use this command, you need to first add a package that implements end-to-end testing capabilities.

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI Overview and Command Reference](https://angular.io/cli) page.


Generate Model and APi

npx orval --config ./orval.config.ts


# build to registry
``` sh
docker build -t lifelong-student-api:0.0.1-beta.2-alpha.17 .
docker image tag lifelong-student-api:0.0.1-beta.2-alpha.17 hubcpa.ar.co.th:5000/lifelong-student-api:0.0.1-beta.2-alpha.17
docker push hubcpa.ar.co.th:5000/lifelong-student-api:0.0.1-beta.2-alpha.17

UAT

docker build -t lifelong-student-api:0.0.1-beta.2-alpha.17 . &&
docker image tag lifelong-student-api:0.0.1-beta.2-alpha.17 hubcpa.swu.ac.th/lifelong-student-api:0.0.1-beta.2-alpha.17 &&
docker push hubcpa.swu.ac.th/lifelong-student-api:0.0.1-beta.2-alpha.17
```

Installing the Kubernetes Nginx Ingress Controller

helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm install nginx-ingress ingress-nginx/ingress-nginx --set controller.publishService.enabled=true

kubectl delete -A validatingwebhookconfigurations nginx-ingress-ingress-nginx-admission

NAME: nginx-ingress
LAST DEPLOYED: Sat Jan  6 24:04:27 2024
NAMESPACE: default
STATUS: deployed
REVISION: 1
TEST SUITE: None
NOTES:
The ingress-nginx controller has been installed.
It may take a few minutes for the load balancer IP to be available.
You can watch the status by running 'kubectl get service --namespace default nginx-ingress-ingress-nginx-controller --output wide --watch'

An example Ingress that makes use of the controller:
  apiVersion: networking.k8s.io/v1
  kind: Ingress
  metadata:
    name: example
    namespace: foo
  spec:
    ingressClassName: nginx
    rules:
      - host: www.example.com
        http:
          paths:
            - pathType: Prefix
              backend:
                service:
                  name: exampleService
                  port:
                    number: 80
              path: /
    # This section is only required if TLS is to be enabled for the Ingress
    tls:
      - hosts:
        - www.example.com
        secretName: example-tls

If TLS is enabled for the Ingress, a Secret containing the certificate and key must also be provided:

  apiVersion: v1
  kind: Secret
  metadata:
    name: example-tls
    namespace: foo
  data:
    tls.crt: <base64 encoded cert>
    tls.key: <base64 encoded key>
  type: kubernetes.io/tls


  