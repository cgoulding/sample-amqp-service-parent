package com.monadiccloud.sample.applications.amqpservice.client;

import com.monadiccloud.core.amqp.client.exception.ServiceExecutionException;
import com.monadiccloud.core.amqp.client.exception.ServiceTimeoutException;
import com.monadiccloud.core.amqp.context.ApplicationConfiguration;
import com.monadiccloud.core.amqp.context.ApplicationConfigurationFactory;
import com.monadiccloud.sample.applications.amqpservice.client.api.SampleResponse;
import com.monadiccloud.sample.applications.amqpservice.client.config.PropertiesConfig;
import org.junit.Test;
import org.springframework.util.Assert;

/**
 * @author Connor Goulding
 */
public class SampleServiceClientFactoryIntegrationTest {
    @Test
    public void testCreate() throws ServiceExecutionException, ServiceTimeoutException {
        ApplicationConfiguration applicationConfiguration = ApplicationConfigurationFactory.getInstance()
                .createApplicationConfiguration("SampleServiceClientFactoryIntegrationTest");

        SampleServiceClientFactory factory = new SampleServiceClientFactory();
        SampleServiceClient client = factory.createClient(applicationConfiguration, PropertiesConfig.class);

        SampleResponse response = client.request("abcd", 5000l);
        Assert.notNull(response.getCorrelationId());
    }
}
