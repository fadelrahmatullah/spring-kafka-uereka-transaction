package com.consumer.app.service;

import com.core.app.kafkainstance.Consumer;
import com.core.app.service.BaseService;

public interface ConsumerValidationService extends BaseService{
    void validationConsumer(Consumer consumer);
}
