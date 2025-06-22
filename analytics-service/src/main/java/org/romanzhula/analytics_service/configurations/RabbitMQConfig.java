package org.romanzhula.analytics_service.configurations;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    public static final String ANALYTICS_EXCHANGE = "analytics-exchange";
    public static final String ANALYTICS_QUEUE = "analytics-queue";
    public static final String ANALYTICS_ROUTING_KEY = "analytics.routing.key";


    @Bean
    public TopicExchange analyticsExchange() {
        return new TopicExchange(ANALYTICS_EXCHANGE);
    }

    @Bean
    public Queue analyticsQueue() {
        return new Queue(ANALYTICS_QUEUE, true);
    }

    @Bean
    public Binding analyticsBinding(Queue analyticsQueue, TopicExchange analyticsExchange) {
        return BindingBuilder
                .bind(analyticsQueue)
                .to(analyticsExchange)
                .with(ANALYTICS_ROUTING_KEY)
        ;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitListenerErrorHandler rabbitErrorHandler() {
        return (channel, amqpMessage, message, exception) -> {
            throw new AmqpRejectAndDontRequeueException("Error processing message", exception);
        };
    }

}
