package com.monadiccloud.sample.applications.amqpservice;

import com.monadiccloud.core.amqp.context.AmqpContext;
import com.monadiccloud.sample.applications.amqpservice.config.AmqpConfig;
import com.monadiccloud.sample.applications.amqpservice.config.PropertiesConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext configApplicationContext = new AnnotationConfigApplicationContext(PropertiesConfig.class, AmqpConfig.class);
        AmqpContext context = configApplicationContext.getBean(AmqpContext.class);
        context.start();
    }
}
