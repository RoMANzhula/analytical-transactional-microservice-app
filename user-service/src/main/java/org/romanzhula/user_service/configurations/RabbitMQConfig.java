package org.romanzhula.user_service.configurations;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    public static final String USER_EXCHANGE = "user-exchange";
    public static final String USER_INFO_QUEUE = "user-queue";
    public static final String USER_ROUTING_KEY = "user.routing.key";


    @Bean
    public TopicExchange userInfoExchenge() {
        return new TopicExchange(USER_EXCHANGE);
    }

    @Bean
    public Queue userInfoQueue() {
        return new Queue(USER_INFO_QUEUE, true);
    }

    @Bean
    public Binding userBinding(Queue userInfoQueue, TopicExchange userInfoExchange) {
        return BindingBuilder
                .bind(userInfoQueue)
                .to(userInfoExchange)
                .with(USER_ROUTING_KEY)
        ;
    }

}
