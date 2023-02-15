package com.ruoyi.system.service.impl;

import com.ruoyi.common.config.RabbitConfig;
import com.ruoyi.common.constant.MessageHelper;
import com.ruoyi.common.domain.Mail;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IProduceService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProduceServiceImpl implements IProduceService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public boolean send(Mail mail) {
        //创建uuid
        String msgId = UUID.randomUUID().toString().replaceAll("-", "");
        mail.setMsgId(msgId);
        //发送消息到mq
        CorrelationData correlationData = new CorrelationData(msgId);
        rabbitTemplate.convertAndSend(RabbitConfig.MAIL_EXCHANGE_NAME, RabbitConfig.MAIL_ROUTING_KEY_NAME, MessageHelper.objToMsg(mail), correlationData);
        return true;
    }
}
