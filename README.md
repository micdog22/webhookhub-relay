# WebhookHub — Webhooks Relay com Retentativa e Assinatura (Java • Spring Boot)

Servidor auto-hospedado para receber eventos e entregar webhooks a endpoints assinantes, com retentativa exponencial e assinatura HMAC.

## Como rodar
```
cd webhookhub-relay
mvn -q -DskipTests package
java -jar target/webhookhub-0.1.0.jar
```
