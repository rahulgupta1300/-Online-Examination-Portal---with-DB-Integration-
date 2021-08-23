#!/usr/bin/env bash

kubectl delete namespace sg-exam

docker rmi $(docker images | grep "none" | awk '{print $3}')

kubectl create namespace sg-exam

kubectl config set-context $(kubectl config current-context) --namespace=sg-exam

kubectl create configmap config-repo-auth-service      --from-file=config-repo/application.yml --from-file=config-repo/auth-service.yml --save-config
kubectl create configmap config-repo-exam-service      --from-file=config-repo/application.yml --from-file=config-repo/exam-service.yml --save-config
kubectl create configmap config-repo-msc-service      --from-file=config-repo/application.yml --from-file=config-repo/msc-service.yml --save-config
kubectl create configmap config-repo-user-service      --from-file=config-repo/application.yml --from-file=config-repo/user-service.yml --save-config

kubectl create secret generic config-server-secrets --from-literal=ENCRYPT_KEY=my-very-secure-encrypt-key --from-literal=SPRING_SECURITY_USER_NAME=dev-usr --from-literal=SPRING_SECURITY_USER_PASSWORD=dev-pwd --save-config

kubectl create secret generic redis-credentials --from-literal=SPRING_REDIS_HOST=redis --from-literal=SPRING_REDIS_PORT=6379 --save-config

kubectl create secret generic mysql-credentials --from-literal=SPRING_DATASOURCE_USERNAME=root --from-literal=SPRING_DATASOURCE_PASSWORD=sg-exam --save-config

kubectl label namespace sg-exam istio-injection=enabled

kubectl create secret tls tls-certificate --key kubernetes/cert/tls.key --cert kubernetes/cert/tls.crt

kubectl apply -k kubernetes/services/overlays/dev

kubectl wait --timeout=600s --for=condition=ready pod --all