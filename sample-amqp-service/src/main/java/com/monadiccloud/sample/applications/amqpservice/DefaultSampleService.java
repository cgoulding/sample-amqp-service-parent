package com.monadiccloud.sample.applications.amqpservice;

import java.util.UUID;

public class DefaultSampleService implements SampleService {
    @Override
    public String createContent() {
        return UUID.randomUUID().toString();
    }
}
