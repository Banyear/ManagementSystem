package com.ruoyi.system.service;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;


public interface IConsumerService {
    void consume(Message message, Channel channel);
}
