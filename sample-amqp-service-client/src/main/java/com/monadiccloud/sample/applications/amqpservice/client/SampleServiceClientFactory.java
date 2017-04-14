package com.monadiccloud.sample.applications.amqpservice.client;

import com.monadiccloud.core.amqp.client.rpc.DelegatingMessageConsumer;
import com.monadiccloud.core.amqp.context.AmqpContext;
import com.monadiccloud.core.amqp.context.ApplicationConfiguration;
import com.monadiccloud.core.amqp.context.ApplicationConfigurationContext;
import com.monadiccloud.sample.applications.amqpservice.client.config.AmqpConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SampleServiceClientFactory {
    public SampleServiceClient createClient(ApplicationConfiguration applicationConfiguration, Class... propertySourceConfigurations) {
        ApplicationConfigurationContext.setCurrent(applicationConfiguration);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        final String activeProfile = System.getProperty("springProfile", "default");
        context.getEnvironment().setActiveProfiles(activeProfile);
        context.register(AmqpConfig.class);

        for (Class configurationClass : propertySourceConfigurations) {
            context.register(configurationClass);
        }
        context.refresh();

        AmqpContext rabbitContext = context.getBean(AmqpContext.class);
        DelegatingMessageConsumer consumer = context.getBean(DelegatingMessageConsumer.class);
        SampleServiceClient client = new SampleServiceClient(rabbitContext, consumer);

        rabbitContext.start();
        return client;
    }
}
