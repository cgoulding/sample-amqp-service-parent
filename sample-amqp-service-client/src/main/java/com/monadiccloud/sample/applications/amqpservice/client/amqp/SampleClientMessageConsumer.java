package com.monadiccloud.sample.applications.amqpservice.client.amqp;

import com.monadiccloud.core.amqp.client.rpc.DefaultMessageConsumer;
import com.monadiccloud.core.amqp.client.rpc.DelegatingMessageConsumer;
import com.monadiccloud.core.amqp.client.rpc.ServiceCallbackAdapter;
import com.monadiccloud.core.amqp.consumer.handler.AmqpContextAwareMessageHandler;
import com.monadiccloud.core.amqp.context.AmqpContext;

/**
 * @author Connor Goulding
 */
public class SampleClientMessageConsumer implements DelegatingMessageConsumer, AmqpContextAwareMessageHandler {
    private final DefaultMessageConsumer delegatingMessageConsumer = new DefaultMessageConsumer();

    @Override
    public void handleMessage(Object o) {
        delegatingMessageConsumer.handleMessage(o);
    }

    @Override
    public <S, D> void addAdapter(ServiceCallbackAdapter<S, D> serviceCallbackAdapter) {
        delegatingMessageConsumer.addAdapter(serviceCallbackAdapter);
    }

    @Override
    public void setAmqpContext(AmqpContext amqpContext) {

    }
}
