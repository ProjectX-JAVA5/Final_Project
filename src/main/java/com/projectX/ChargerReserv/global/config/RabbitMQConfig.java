package com.projectX.ChargerReserv.global.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String RESERVATION_QUEUE = "reservationQueue";
    public static final String RESERVATION_DLQ = "reservationDLQ";
    public static final String RESERVATION_EXCHANGE = "reservationExchange";
    public static final String RESERVATION_DLQ_EXCHANGE = "reservationDLQExchange";
    public static final String RESERVATION_ROUTING_KEY = "reservation.key";
    public static final String RESERVATION_DLQ_ROUTING_KEY = "reservation.dlq.key";

    @Bean
    public Queue reservationQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", RESERVATION_DLQ_EXCHANGE);
        args.put("x-dead-letter-routing-key", RESERVATION_DLQ_ROUTING_KEY);
        args.put("x-message-ttl", 2 * 60 * 1000); // 2분 TTL (테스트용)
        return QueueBuilder.durable(RESERVATION_QUEUE).withArguments(args).build();
    }

    @Bean
    public Queue reservationDLQ() {
        return QueueBuilder.durable(RESERVATION_DLQ).build();
    }

    @Bean
    public DirectExchange reservationExchange() {
        return new DirectExchange(RESERVATION_EXCHANGE);
    }

    @Bean
    public DirectExchange reservationDLQExchange() {
        return new DirectExchange(RESERVATION_DLQ_EXCHANGE);
    }

    @Bean
    public Binding reservationBinding(Queue reservationQueue, DirectExchange reservationExchange) {
        return BindingBuilder.bind(reservationQueue).to(reservationExchange).with(RESERVATION_ROUTING_KEY);
    }

    @Bean
    public Binding reservationDLQBinding(Queue reservationDLQ, DirectExchange reservationDLQExchange) {
        return BindingBuilder.bind(reservationDLQ).to(reservationDLQExchange).with(RESERVATION_DLQ_ROUTING_KEY);
    }
}
