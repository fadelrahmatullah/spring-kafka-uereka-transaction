package com.consumer.app.job.exec;

import com.consumer.app.entity.TaskEntity;
import com.consumer.app.service.TaskTransactionExecutor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractExecutor implements TaskTransactionExecutor{

    @Override
    public void validationTask(TaskEntity taskData) throws Exception {
        try {
           this.doWork(taskData);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
    protected abstract void doWork(TaskEntity taskData) throws Exception;

    @Override
    public void thirdPartyLocation(TaskEntity taskData) throws Exception {
        try {
            this.doWorkGetLocation(taskData);
        } catch (Exception e) {
            throw e;
        }
    }
    
    protected abstract void doWorkGetLocation(TaskEntity taskData) throws Exception;
    
}
