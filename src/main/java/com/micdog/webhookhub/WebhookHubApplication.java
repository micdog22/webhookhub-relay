package com.micdog.webhookhub;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@SpringBootApplication @EnableScheduling
public class WebhookHubApplication { public static void main(String[] args){ SpringApplication.run(WebhookHubApplication.class, args); } }
