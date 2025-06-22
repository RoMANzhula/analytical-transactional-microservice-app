package org.romanzhula.notification_service.configurations;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;


@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.exchange.notification.name}")
    private String notificationExchange;

    @Value("${spring.rabbitmq.queue.notification}")
    private String notificationQueue;

    @Value("${spring.rabbitmq.routing-key.notification}")
    private String notificationRoutingKey;


    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(notificationExchange);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(notificationQueue, true);
    }

    @Bean
    public Binding notificationBinding(
            Queue notificationQueue,
            TopicExchange notificationExchange
    ) {
        return BindingBuilder
                .bind(notificationQueue)
                .to(notificationExchange)
                .with(notificationRoutingKey)
        ;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitListenerErrorHandler rabbitErrorHandler() {
        return (channel, amqpMessage, message, exception) -> {
            throw new AmqpRejectAndDontRequeueException("Error processing message. Notification service.", exception);
        };
    }

}
