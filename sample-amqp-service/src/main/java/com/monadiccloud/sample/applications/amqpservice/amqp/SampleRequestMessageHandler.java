package com.monadiccloud.sample.applications.amqpservice.amqp;

import com.monadiccloud.core.amqp.consumer.handler.AmqpContextAwareMessageHandler;
import com.monadiccloud.core.amqp.context.AmqpContext;
import com.monadiccloud.sample.applications.amqpservice.SampleService;
import com.monadiccloud.sample.applications.amqpservice.api.SampleRequest;
import com.monadiccloud.sample.applications.amqpservice.api.SampleResponse;

import java.util.Arrays;
import java.util.Calendar;

public class SampleRequestMessageHandler implements AmqpContextAwareMessageHandler {

    private final SampleService sampleService;
    private final SampleServiceProducer sampleServiceProducer;

    public SampleRequestMessageHandler(SampleService sampleService, SampleServiceProducer sampleServiceProducer) {
        this.sampleService = sampleService;
        this.sampleServiceProducer = sampleServiceProducer;
    }

    @Override
    public void handleMessage(Object o) throws Exception {
        final SampleRequest requestMessage = (SampleRequest) o;
        String identifier = sampleService.createContent();
        SampleResponse response = new SampleResponse(Calendar.getInstance().getTime(), requestMessage.getCorrelationId(),
                Arrays.asList(identifier));
        sampleServiceProducer.sendMessage(response, requestMessage.getReplyTo());
    }

    @Override
    public void setAmqpContext(AmqpContext amqpContext) {

    }
}
