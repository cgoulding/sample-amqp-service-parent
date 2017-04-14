package com.monadiccloud.sample.applications.amqpservice.config;

import com.monadiccloud.core.amqp.config.DefaultRabbitMqPropertiesConfig;
import com.monadiccloud.core.amqp.connectors.RabbitMqCachingConnectionFactory;
import com.monadiccloud.core.amqp.context.AmqpContext;
import com.monadiccloud.core.amqp.context.AmqpContextListener;
import com.monadiccloud.core.amqp.context.ApplicationConfiguration;
import com.monadiccloud.core.amqp.context.ApplicationConfigurationFactory;
import com.monadiccloud.core.amqp.context.builder.AmqpContextBuilder;
import com.monadiccloud.core.amqp.context.builder.MessageMetaData;
import com.monadiccloud.core.amqp.context.builder.MessageMetaDataReader;
import com.monadiccloud.sample.applications.amqpservice.DefaultSampleService;
import com.monadiccloud.sample.applications.amqpservice.SampleService;
import com.monadiccloud.sample.applications.amqpservice.amqp.SampleRequestMessageHandler;
import com.monadiccloud.sample.applications.amqpservice.amqp.SampleServiceProducer;
import com.monadiccloud.sample.applications.amqpservice.api.SampleRequest;
import com.monadiccloud.sample.applications.amqpservice.api.SampleResponse;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.util.Collection;

@Configuration
@Import(PropertiesConfig.class)
public class AmqpConfig {
    @Bean
    public AmqpContext amqpContext(@Autowired ConnectionFactory rabbitConnectionFactory,
                                   @Autowired SampleServiceProducer sampleServiceProducer,
                                   @Autowired SampleService sampleService) throws IOException {
        ApplicationConfiguration applicationConfiguration = ApplicationConfigurationFactory.getInstance()
                .createApplicationConfiguration("sampleApplication");

        MessageMetaDataReader reader = new MessageMetaDataReader();
        Collection<MessageMetaData> metaDatas = reader.read(getClass().getClassLoader().getResourceAsStream(
                "META-INF/spring/sample-amqp-service/amqp.json"));

        AmqpContextBuilder builder = new AmqpContextBuilder(rabbitConnectionFactory, applicationConfiguration, metaDatas);
        builder.consumes("queue.monadiccloud.sample.request", true, new SampleRequestMessageHandler(sampleService, sampleServiceProducer),
                SampleRequest.class);
        builder.produces(SampleResponse.class);

        return builder.build();
    }

    @Bean
    public AmqpContextListener rabbitContextListener() {
        return new AmqpContextListener();
    }

    @Bean
    public ConnectionFactory productionCachingConnectionFactory(@Autowired Environment environment) {
        final com.rabbitmq.client.ConnectionFactory connectionFactory = new com.rabbitmq.client.ConnectionFactory();

        final RabbitMqCachingConnectionFactory rabbitMqCachingConnectionFactory =
                new RabbitMqCachingConnectionFactory(connectionFactory, new DefaultRabbitMqPropertiesConfig(environment));
        return rabbitMqCachingConnectionFactory;
    }

    @Bean
    public SampleServiceProducer sampleServiceProducer() {
        return new SampleServiceProducer();
    }

    @Bean
    public SampleService sampleService() {
        return new DefaultSampleService();
    }
}
