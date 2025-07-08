package ru.difembaxio.util;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ru.difembaxio.model.UserRabbitDto;

@Component
@EnableRabbit
@RequiredArgsConstructor
public class RabbitMqListener {

    private final RabbitTemplate rabbitTemplate;
    private static final String QUEUE1 = "queue1";
    private static final String EXCHANGE_NAME = "myExchange";

    private static final String ROUTING_KEY = "routingKeyQueue1";

    @RabbitListener(queues = QUEUE1)
    public void sendMessage(UserRabbitDto user) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, user);
    }
}
