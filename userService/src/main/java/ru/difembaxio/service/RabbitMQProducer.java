package ru.difembaxio.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.difembaxio.dto.UserRabbitDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQProducer {

    private static final String EXCHANGE_NAME = "myExchange";

    private static final String ROUTING_KEY = "routingKeyQueue1";

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(UserRabbitDto rabbitDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, rabbitDto);
    }
}
