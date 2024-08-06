@echo off
echo Applying new resources...

minikube image load account_service:latest
minikube image load customer_service:latest

kubectl delete -f customer-service-deployment.yml
kubectl delete -f customer-service-service.yml
kubectl delete -f account-service-deployment.yml
kubectl delete -f account-service-service.yml

kubectl apply -f customer-service-deployment.yml
kubectl apply -f customer-service-node-port.yml
kubectl apply -f account-service-deployment.yml
kubectl apply -f account-service-node-port.yml


echo Done!
