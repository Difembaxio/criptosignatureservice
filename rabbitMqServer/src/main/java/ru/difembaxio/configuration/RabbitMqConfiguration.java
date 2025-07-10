package ru.difembaxio.configuration;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {

    private static final String QUEUE1 = "queue1";
    private static final String QUEUE2 = "queue2";
    private static final String QUEUE3 = "queue3";
    private static final String EXCHANGE_NAME = "myExchange";

    private static final String ROUTING_KEY = "routingKeyQueue1";

    @Value("localhost")
    private String rabbitHost;

    @Value("5672")
    private int rabbitPort;

    @Value("guest")
    private String rabbitUserName;

    @Value("guest")
    private String rabbitPassword;
    @Value("/")
    private String rabbitVirtualHost;


    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitHost,rabbitPort);
        connectionFactory.setUsername(rabbitUserName);
        connectionFactory.setPassword(rabbitPassword);
        connectionFactory.setVirtualHost(rabbitVirtualHost);
        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin(){
        return new RabbitAdmin(connectionFactory());
    }
    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory){
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(EXCHANGE_NAME, true,false);
    }

    @Bean
    public Queue myQueue(){
        return new Queue(QUEUE1);
    }


    @Bean
    public Binding bindingQueue1(){
        return BindingBuilder.bind(myQueue())
            .to(directExchange())
            .with(ROUTING_KEY);
    }
}