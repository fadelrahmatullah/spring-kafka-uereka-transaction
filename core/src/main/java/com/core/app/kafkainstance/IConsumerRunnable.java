package com.core.app.kafkainstance;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IConsumerRunnable implements Runnable{

    public static final Logger logger = LoggerFactory.getLogger(IConsumerRunnable.class);

    private final CountDownLatch latch;
    private final IConsumer iConsumer;
    private final String message;

    public IConsumerRunnable(CountDownLatch latch, IConsumer iConsumer, String message) {
        this.latch = latch;
        this.iConsumer = iConsumer;
        this.message = message;
    }

    

    @Override
    public void run() {
        
        try {
            logger.info("IConsumerRunnable : "+message);
            iConsumer.execute(message);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }finally {
            latch.countDown();
        }
        
    }
    
}
