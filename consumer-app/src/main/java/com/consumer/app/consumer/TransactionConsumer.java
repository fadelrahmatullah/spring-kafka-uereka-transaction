package com.consumer.app.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.consumer.app.service.ConsumerValidationService;
import com.core.app.kafkainstance.Consumer;
import com.core.app.kafkainstance.IConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TransactionConsumer implements IConsumer {

    @Autowired
    private ConsumerValidationService consumerValidationService;

    @Override
    public void execute(String message) {
        

        try {
           
            System.out.println(message);
            String cleanJsonString = null;

            if (message.startsWith("\"") && message.endsWith("\"")) {
                cleanJsonString = message.substring(1, message.length() - 1);
                cleanJsonString = cleanJsonString.replace("\\\"", "\"");
            }

            Consumer consume = new Consumer();
            ObjectMapper objectMapper = new ObjectMapper();
            
            try {
                consume = objectMapper.readValue(cleanJsonString, consume.getClass());
            } catch (Exception e) {
                consume = null;
            }
            if (consume == null) {
                throw new RuntimeException("Null Consumer");
            }

            log.info(">> START CONSUME PROCESS : {}", consume.getProcessId());
            Thread.sleep(4000);
            consumerValidationService.validationConsumer(consume);
            
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error Exception {}", e.getMessage());
        }
        
    }

    
    
}
