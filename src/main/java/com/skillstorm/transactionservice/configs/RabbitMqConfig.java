package com.skillstorm.transactionservice.configs;

import com.skillstorm.transactionservice.constants.Queues;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${eureka.instance.hostname}")
    private String host;

    // Exchanges:
    @Value("${exchanges.direct}")
    private String directExchange;

    // Set up credentials and connect to RabbitMQ:
    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    // Configure the RabbitTemplate:
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(messageConverter());
        rabbitTemplate.setReplyTimeout(6000);
        return rabbitTemplate;
    }

    // Create the exchange:
    @Bean
    public Exchange directExchange() {
        return new DirectExchange(directExchange);
    }

    // Create the queues:
    @Bean
    public Queue transactionRequestQueue() {
        return new Queue(Queues.BUDGET_REQUEST.toString());
    }

    @Bean
    public Queue transactionResponseQueue() {
        return new Queue(Queues.BUDGET_RESPONSE.toString());
    }

    @Bean
    public Queue accountRequestQueue() {
        return new Queue(Queues.ACCOUNT_REQUEST.toString());
    }

    @Bean
    public Queue accountResponseQueue() {
        return new Queue(Queues.ACCOUNT_RESPONSE.toString());
    }

    // Bind the queues to the exchange:
    @Bean
    public Binding transactionRequestBinding(Queue transactionRequestQueue, Exchange directExchange) {
        return BindingBuilder.bind(transactionRequestQueue)
                .to(directExchange)
                .with(Queues.BUDGET_REQUEST)
                .noargs();
    }

    @Bean
    public Binding transactionResponseBinding(Queue transactionResponseQueue, Exchange directExchange) {
        return BindingBuilder.bind(transactionResponseQueue)
                .to(directExchange)
                .with(Queues.BUDGET_RESPONSE)
                .noargs();
    }

    @Bean
    public Binding accountRequestBinding(Queue accountRequestQueue, Exchange directExchange) {
        return BindingBuilder.bind(accountRequestQueue)
                .to(directExchange)
                .with(Queues.ACCOUNT_REQUEST)
                .noargs();
    }

    @Bean
    public Binding accountResponseBinding(Queue accountResponseQueue, Exchange directExchange) {
        return BindingBuilder.bind(accountResponseQueue)
                .to(directExchange)
                .with(Queues.ACCOUNT_RESPONSE)
                .noargs();
    }

    // Serialize Java objects to JSON:
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
