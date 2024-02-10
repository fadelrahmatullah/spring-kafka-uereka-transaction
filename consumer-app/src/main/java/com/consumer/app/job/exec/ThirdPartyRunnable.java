package com.consumer.app.job.exec;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.consumer.app.entity.TaskEntity;
import com.consumer.app.service.TaskTransactionExecutor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ThirdPartyRunnable implements Runnable{

    public static final Logger logger = LoggerFactory.getLogger(ThirdPartyRunnable.class);

    private final TaskEntity taskEntity;
    private final CountDownLatch latch;
    private final TaskTransactionExecutor taskExecutor;

    @Override
    public void run() {
        try {
            taskExecutor.thirdPartyLocation(taskEntity);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }finally{
            latch.countDown();
        }
    }
    
}
