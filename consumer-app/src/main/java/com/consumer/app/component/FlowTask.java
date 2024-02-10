package com.consumer.app.component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.consumer.app.entity.TaskEntity;
import com.consumer.app.job.exec.TaskRunnable;
import com.consumer.app.job.exec.ThirdPartyRunnable;
import com.consumer.app.repository.TaskRepository;
import com.consumer.app.service.TaskTransactionExecutor;
import com.core.app.constants.Constants;
import com.core.app.entity.TransactionEntity;
import com.core.app.entity.UserEntity;
import com.core.app.repository.TransactionRepository;
import com.core.app.repository.UserRepository;

@Component
public class FlowTask {
    
    ExecutorService service = Executors.newFixedThreadPool(8);
    ExecutorService serviceThirdParty = Executors.newFixedThreadPool(8);


    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskTransactionExecutor taskTransactionExecutor;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;


    private final Lock lock = new ReentrantLock();
    private final Lock lock2 = new ReentrantLock();

    private List<TaskEntity> getTaskListProcess(){
        List<TaskEntity> resultTask = taskRepository.getTaskByStatus(Constants.PROCESS);
        return resultTask;
    }

    private List<TaskEntity> getTaskLocation(){
        List<TaskEntity> result = taskRepository.getTasksWithoutLocation();
        return result;
    }

    public void flowExecute(){
        
        lock.lock();
        try {

            List<TaskEntity> taskList = this.getTaskListProcess();
            final int taskNum = taskList.size();
            final CountDownLatch latch = new CountDownLatch(taskNum);

            if (taskList.size() > 0) {
                taskList.forEach(task -> {
                    service.execute(() -> {
                        try {
                            new TaskRunnable(task, latch, taskTransactionExecutor).run();
                        } catch (Exception e) {
                            UserEntity user = userRepository.findById(task.getUserId()).get();
                            this.updateTaskFailed(task, e.getMessage(), user.getUserName());

                            TransactionEntity transactionEntity = transactionRepository.findById(task.getTaskId()).get();
                            this.updateTransactionFailed(transactionEntity, e.getMessage());
                        }
                    });
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        
        
    }

    public void taskLocation(){

        lock2.lock();
        try {
            
            List<TaskEntity> taskList = this.getTaskLocation();
            final CountDownLatch latch = new CountDownLatch(taskList.size());
            if (taskList.size() > 0) {

                taskList.forEach(
                    task -> 
                        serviceThirdParty.execute(() -> 
                            {
                                new ThirdPartyRunnable(task, latch, taskTransactionExecutor).run();
                            }
                        )
                );
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            lock2.unlock();
        }
    }
    
    private void updateTransactionFailed(TransactionEntity transactionEntity,  String msg){
        transactionEntity.setStatus(Constants.FAILED);
        transactionEntity.setTransRefresh(false);
        transactionEntity.setMessage(msg);
        transactionEntity.setTryTask((transactionEntity.getTryTask() == null ? 0 : transactionEntity.getTryTask()) + 1);
        transactionRepository.save(transactionEntity);
    }

    private void updateTaskFailed(TaskEntity taskEntity, String msg, String username){
        taskEntity.setMsg(msg);
        taskEntity.setStatus(Constants.FAILED);
        taskEntity.setChangedBy(username);
        taskEntity.setChangedDt(new Date());
        taskRepository.save(taskEntity);
    }


}
