package com.monadiccloud.sample.applications.amqpservice.amqp;

import com.monadiccloud.core.amqp.context.AmqpContext;
import com.monadiccloud.core.amqp.context.AmqpContextAware;
import com.monadiccloud.core.amqp.template.OpinionatedRabbitTemplate;
import com.monadiccloud.sample.applications.amqpservice.api.SampleResponse;

public class SampleServiceProducer implements AmqpContextAware {
    private OpinionatedRabbitTemplate rabbitTemplate;

    public void sendMessage(SampleResponse message, String replyTo) {
        rabbitTemplate.send(message, replyTo);
    }

    @Override
    public void setAmqpContext(AmqpContext amqpContext) {
        rabbitTemplate = new OpinionatedRabbitTemplate(amqpContext);
    }
}
