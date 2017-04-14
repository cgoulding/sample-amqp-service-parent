package com.monadiccloud.sample.applications.amqpservice.client.config;

import com.monadiccloud.core.amqp.config.DefaultRabbitMqPropertiesConfig;
import com.monadiccloud.core.amqp.connectors.RabbitMqCachingConnectionFactory;
import com.monadiccloud.core.amqp.context.AmqpContext;
import com.monadiccloud.core.amqp.context.AmqpContextListener;
import com.monadiccloud.core.amqp.context.ApplicationConfiguration;
import com.monadiccloud.core.amqp.context.ApplicationConfigurationFactory;
import com.monadiccloud.core.amqp.context.builder.AmqpContextBuilder;
import com.monadiccloud.core.amqp.context.builder.MessageMetaData;
import com.monadiccloud.core.amqp.context.builder.MessageMetaDataReader;
import com.monadiccloud.sample.applications.amqpservice.client.amqp.SampleClientMessageConsumer;
import com.monadiccloud.sample.applications.amqpservice.client.api.SampleRequest;
import com.monadiccloud.sample.applications.amqpservice.client.api.SampleResponse;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.util.Collection;

/**
 * @author Connor Goulding
 */
@Configuration
public class AmqpConfig {
    @Bean
    public AmqpContext amqpContext(@Autowired ConnectionFactory rabbitConnectionFactory,
                                   @Autowired SampleClientMessageConsumer consumer) throws IOException {
        ApplicationConfiguration applicationConfiguration = ApplicationConfigurationFactory.getInstance()
                .createApplicationConfiguration("sampleApplicationClient");

        MessageMetaDataReader reader = new MessageMetaDataReader();
        Collection<MessageMetaData> metaDatas = reader.read(getClass().getClassLoader().getResourceAsStream(
                "META-INF/spring/sample-amqp-service-client/amqp.json"));

        AmqpContextBuilder contextBuilder = new AmqpContextBuilder(rabbitConnectionFactory, applicationConfiguration, metaDatas);
        contextBuilder.producesAndConsumes(SampleRequest.class, queueName(applicationConfiguration, "monadiccloud.sample.response"),
                true, consumer, SampleResponse.class);

        return contextBuilder.build();
    }

    @Bean
    public AmqpContextListener rabbitContextListener() {
        return new AmqpContextListener();
    }

    @Bean
    public SampleClientMessageConsumer sampleClientMessageConsumer() {
        return new SampleClientMessageConsumer();
    }

    @Bean
    public ConnectionFactory productionCachingConnectionFactory(@Autowired Environment environment) {
        final com.rabbitmq.client.ConnectionFactory connectionFactory = new com.rabbitmq.client.ConnectionFactory();

        final RabbitMqCachingConnectionFactory rabbitMqCachingConnectionFactory =
                new RabbitMqCachingConnectionFactory(connectionFactory, new DefaultRabbitMqPropertiesConfig(environment));
        return rabbitMqCachingConnectionFactory;
    }

    private String queueName(ApplicationConfiguration appConfig, String base) {
        return "queue." + base + "." + appConfig.getApplicationName();
    }
}
