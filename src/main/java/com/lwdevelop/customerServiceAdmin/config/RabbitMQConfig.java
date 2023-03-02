package com.lwdevelop.customerServiceAdmin.config;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    
    @Value("${spring.rabbitmq.host}")
    private String rabbitMQHost;
    
    @Value("${spring.rabbitmq.port}")
    private int rabbitMQPort;
    
    @Value("${spring.rabbitmq.username}")
    private String rabbitMQUsername;
    
    @Value("${spring.rabbitmq.password}")
    private String rabbitMQPassword;
    
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitMQHost);
        connectionFactory.setPort(rabbitMQPort);
        connectionFactory.setUsername(rabbitMQUsername);
        connectionFactory.setPassword(rabbitMQPassword);
        return connectionFactory;
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
    
    @Bean
    public Queue queue() {
        return new Queue("chat-messages");
    }
    
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("chat-exchange");
    }
    
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(topicExchange()).with("chat-messages.#");
    }
    
}