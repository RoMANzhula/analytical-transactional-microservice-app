package org.romanzhula.transaction_service.configurations;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;


@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.exchange.notification.name}")
    private String notificationExchangeName;

    @Value("${spring.rabbitmq.queue.notification}")
    private String notificationQueueName;

    @Value("${spring.rabbitmq.routing-key.notification}")
    private String notificationRoutingKey;

    @Value("${spring.rabbitmq.exchange.analytics.name}")
    private String analyticsExchangeName;

    @Value("${spring.rabbitmq.queue.analytics}")
    private String analyticsQueueName;

    @Value("${spring.rabbitmq.routing-key.analytics}")
    private String analyticsRoutingKey;


    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(notificationExchangeName);
    }

    @Bean
    public TopicExchange analyticsExchange() {
        return new TopicExchange(analyticsExchangeName);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(notificationQueueName, true);
    }

    @Bean
    public Queue transactionQueue() {
        return new Queue(analyticsQueueName, true);
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder
                .bind(notificationQueue)
                .to(notificationExchange)
                .with(notificationRoutingKey)
        ;
    }

    @Bean
    public Binding transactionBinding(Queue transactionQueue, TopicExchange analyticsExchange) {
        return BindingBuilder
                .bind(transactionQueue)
                .to(analyticsExchange)
                .with(analyticsRoutingKey)
        ;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ErrorHandler rabbitErrorHandler() {
        return t -> {
            throw new AmqpRejectAndDontRequeueException("Error processing message", t);
        };
    }

}
