package com.monadiccloud.sample.applications.amqpservice.client.adapters;

import com.monadiccloud.core.amqp.client.callback.ServiceCallback;
import com.monadiccloud.core.amqp.client.callback.ServiceResponse;
import com.monadiccloud.core.amqp.client.rpc.ServiceCallbackAdapter;
import com.monadiccloud.core.amqp.client.rpc.ServiceCallbackRegistry;
import com.monadiccloud.sample.applications.amqpservice.client.api.SampleResponse;

/**
 * @author Connor Goulding
 */
public class SampleResponseMessageAdapter implements ServiceCallbackAdapter<SampleResponse, ServiceResponse<SampleResponse>> {
    private final ServiceCallbackRegistry serviceCallbackRegistry;

    public SampleResponseMessageAdapter(ServiceCallbackRegistry serviceCallbackRegistry) {
        this.serviceCallbackRegistry = serviceCallbackRegistry;
    }

    @Override
    public ServiceResponse<SampleResponse> transform(SampleResponse sampleResponse) {
        return new ServiceResponse<>(sampleResponse.getCorrelationId(), sampleResponse, null);
    }

    @Override
    public void consume(ServiceCallback serviceCallback, ServiceResponse<SampleResponse> sampleResponseServiceResponse) {
        serviceCallback.handleServiceResponse(sampleResponseServiceResponse);
    }

    @Override
    public ServiceCallback take(SampleResponse sampleResponse) {
        return serviceCallbackRegistry.removeServiceCallback(sampleResponse.getCorrelationId());
    }

    @Override
    public Class<SampleResponse> getSourceClass() {
        return SampleResponse.class;
    }
}
