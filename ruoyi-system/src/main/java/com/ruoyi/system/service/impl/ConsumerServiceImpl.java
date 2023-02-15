package com.ruoyi.system.service.impl;

import com.rabbitmq.client.Channel;
import com.ruoyi.common.config.RabbitConfig;
import com.ruoyi.common.domain.Mail;
import com.ruoyi.common.utils.JsonUtil;
import com.ruoyi.common.utils.mail.MailUtil;
import com.ruoyi.system.service.IConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class ConsumerServiceImpl implements IConsumerService {
    private static final Logger log = LoggerFactory.getLogger(ConsumerServiceImpl.class);

    @Autowired
    private MailUtil sendMailUtil;

    @RabbitListener(queues = RabbitConfig.MAIL_QUEUE_NAME)
    @Override
    public void consume(Message message, Channel channel) {
        try {
        //将消息转化为对象
        String str = new String(message.getBody());
        Mail mail = JsonUtil.strToObj(str, Mail.class);
        log.info("收到消息: {}", mail.toString());

        MessageProperties properties = message.getMessageProperties();
        long tag = properties.getDeliveryTag();

        boolean success = sendMailUtil.send(mail);
        if (success) {
            channel.basicAck(tag, false);// 消费确认
        } else {
            channel.basicNack(tag, false, true);
        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
