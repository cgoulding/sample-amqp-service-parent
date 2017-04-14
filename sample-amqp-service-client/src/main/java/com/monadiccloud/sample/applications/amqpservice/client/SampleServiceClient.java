package com.monadiccloud.sample.applications.amqpservice.client;

import com.monadiccloud.core.amqp.client.callback.ServiceResponse;
import com.monadiccloud.core.amqp.client.exception.ServiceExecutionException;
import com.monadiccloud.core.amqp.client.exception.ServiceTimeoutException;
import com.monadiccloud.core.amqp.client.rpc.AbstractServiceClient;
import com.monadiccloud.core.amqp.client.rpc.DelegatingMessageConsumer;
import com.monadiccloud.core.amqp.client.rpc.ServiceRequestCallback;
import com.monadiccloud.core.amqp.context.AmqpContext;
import com.monadiccloud.core.amqp.template.OpinionatedRabbitTemplate;
import com.monadiccloud.sample.applications.amqpservice.client.adapters.SampleResponseMessageAdapter;
import com.monadiccloud.sample.applications.amqpservice.client.api.SampleRequest;
import com.monadiccloud.sample.applications.amqpservice.client.api.SampleResponse;

import java.util.Arrays;

public class SampleServiceClient extends AbstractServiceClient {
    private final DelegatingMessageConsumer consumer;
    private final OpinionatedRabbitTemplate template;
    private final AmqpContext amqpContext;

    SampleServiceClient(AmqpContext amqpContext, DelegatingMessageConsumer consumer) {
        this.consumer = consumer;
        this.amqpContext = amqpContext;
        this.template = new OpinionatedRabbitTemplate(amqpContext);

        initCallbacks();
    }

    private void initCallbacks() {
        this.consumer.addAdapter(new SampleResponseMessageAdapter(this));
    }

    public SampleResponse request(final String arg, final long timeout) throws ServiceExecutionException, ServiceTimeoutException {
        final SampleRequest request = new SampleRequest(timestamp(), uuid(), replyTo(SampleRequest.class, SampleResponse.class),
                Arrays.asList(arg));

        ServiceResponse<?> response = processRequest(timeout, new ServiceRequestCallback() {
            @Override
            public String getRequestId() {
                return request.getCorrelationId();
            }

            @Override
            public void executeRequest(String requestId) throws Exception {
                template.send(request);
            }
        });

        return processResponse(response, SampleResponse.class);
    }

    private String replyTo(Class request, Class reply) {
        return amqpContext.getReplyTo(request, reply);
    }

    private <R> R processResponse(ServiceResponse<?> response, Class<R> expectedResponse) throws ServiceExecutionException {
        Object responseMessage = response.getResponse();
        if (responseMessage == null) {
            return null;
        }

        if (expectedResponse.isAssignableFrom(responseMessage.getClass())) {
            return (R) responseMessage;
        } else {
            throw new UnsupportedOperationException("Unexpected response message: " + responseMessage);
        }
    }
}
