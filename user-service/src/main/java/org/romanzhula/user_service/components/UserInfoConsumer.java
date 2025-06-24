package org.romanzhula.user_service.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.romanzhula.user_service.dto.events.UserInfoMessage;
import org.romanzhula.user_service.services.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class UserInfoConsumer {

    private final UserService userService;

    
    @RabbitListener(queues = "${spring.rabbitmq.queue.name.user-info}")
    public void consumeUserInfo(UserInfoMessage userInfoMessage) {
        log.info("Got message: {}", userInfoMessage);
        userService.saveOrUpdateUser(userInfoMessage);
    }

}
