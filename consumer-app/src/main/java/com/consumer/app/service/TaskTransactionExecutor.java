package com.consumer.app.service;

import com.consumer.app.entity.TaskEntity;

public interface TaskTransactionExecutor {
    void validationTask(TaskEntity taskData) throws Exception;
    void thirdPartyLocation(TaskEntity taskData) throws Exception;
}
