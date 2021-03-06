package com.monadiccloud.sample.applications.amqpservice.client.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * @author Connor Goulding
 */
@Configuration
@PropertySources({
        @PropertySource(value = "classpath:META-INF/spring/sample-amqp-service-client/rabbitmq.properties"),
        @PropertySource(value = "file:/opt/monadiccloud/sample/conf/rabbitmq.properties", ignoreResourceNotFound = true)
})
public class PropertiesConfig {
}
