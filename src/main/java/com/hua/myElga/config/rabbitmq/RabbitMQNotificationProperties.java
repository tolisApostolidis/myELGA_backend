package com.hua.myElga.config.rabbitmq;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rabbitmq.notification")
public record RabbitMQNotificationProperties(
        String exchange,
        String queue,
        String routingKey
) {
}
