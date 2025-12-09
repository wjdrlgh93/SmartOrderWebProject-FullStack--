package org.spring.backendspring.rabbitmqWebsocket.config;


import org.spring.backendspring.rabbitmqWebsocket.chat.rabbitmq.Receiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import lombok.RequiredArgsConstructor;

@Configuration
@EnableRabbit
@RequiredArgsConstructor
public class RabbitConfig {

    private final ConnectionFactory connectionFactory;

    @Value("${spring.rabbitmq.crew.exchange}")
    private String crewExchangeYml;

    @Value("${spring.rabbitmq.crew.queue}")
    private String crewQueueYml;

    @Bean
    public Queue crewQueue() {
        return new Queue(crewQueueYml, true);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(crewExchangeYml);
    }

     @Bean
    public Binding crewBinding() {
        // crew.# 로 다 받을거면 이렇게도 가능
        return BindingBuilder.bind(crewQueue())
                .to(topicExchange())
                .with("crew.#");
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }



    @Bean
    public SimpleRabbitListenerContainerFactory
            rabbitFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        configurer.configure(factory, connectionFactory);

        return factory;
    }

    // @Bean
    // public SimpleMessageListenerContainer
    //         messageContainer(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {

    //     SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

    //     container.setConnectionFactory(connectionFactory); //rabbitmq연결
    //     container.setQueueNames(crewQueueYml); //어떤큐?
    //     container.setMessageListener(listenerAdapter); //메시지를 어떻게 처리할지

    //     return container;
    // }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // @Bean
    // public MessageListenerAdapter listenerAdapter(Receiver receiver) {
        
    //     MessageListenerAdapter adapter = new MessageListenerAdapter(receiver, "receiveMessage");

    //     adapter.setMessageConverter(messageConverter());

    //     return adapter;
    // }
}