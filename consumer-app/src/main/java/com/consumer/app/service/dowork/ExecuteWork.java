package com.consumer.app.service.dowork;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.consumer.app.entity.LocationEntity;
import com.consumer.app.entity.TaskEntity;
import com.consumer.app.feign.SourceLocationClient;
import com.consumer.app.job.exec.AbstractExecutor;
import com.consumer.app.repository.LocationRepository;
import com.consumer.app.repository.TaskRepository;
import com.core.app.constants.Constants;
import com.core.app.constants.ResponseStatus;
import com.core.app.entity.BankAccountEntity;
import com.core.app.entity.TransactionEntity;
import com.core.app.repository.BankAccountRepository;
import com.core.app.repository.TransactionRepository;
import com.core.app.response.Response;
import com.core.app.vo.LocationVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ExecuteWork extends AbstractExecutor{

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private SourceLocationClient sourceLocationClient;

    private final Lock lock = new ReentrantLock();

    @Override
    @Modifying
    protected void doWork(TaskEntity taskData) throws Exception {

        Optional<TransactionEntity> transactionEntity = transactionRepository.findById(taskData.getTransactionId());
        TransactionEntity transaction = transactionEntity.get();

        if (!transaction.getTransRefresh()) {
            BankAccountEntity bankAccountEntity = bankAccountRepository.getByAccountNumber(transaction.getAccountNumber());
            log.info(">> Update Saldo For Process : {}", transaction.getProcessId());
            this.calculationSaldo(transaction, bankAccountEntity, taskData);
        }

        return;
    }

    @Override
    @Transactional
    protected void doWorkGetLocation(TaskEntity taskData) throws Exception {

        Response<LocationVo> response;
        TransactionEntity transaction;
        
        try {

            Optional<TransactionEntity> transactionEntity = transactionRepository.findById(taskData.getTransactionId());
            transaction = transactionEntity.get();
            String ipAddress = transaction.getIpAddress();
            
            try {
                lock.lock();
                CompletableFuture<Response<LocationVo>> futureResponse = CompletableFuture.supplyAsync(() -> {
                    return sourceLocationClient.appProduceContollerGetLocation(ipAddress);
                });
    
                response = futureResponse.get();
    
                if (response == null || !response.getStatus().equals(ResponseStatus.SUCCESS)) {
                    lock.unlock();
                    throw new RuntimeException("Response is Null Or Failed");
                }
    
                log.info("Response appProduceContollerGetLocation : {}", response);
                this.insertLocation(response.getData(), transaction, taskData.getTaskId());
            } finally{
                lock.unlock();
            }

        } catch (Exception e) {
            log.info("Response appProduceContollerGetLocation : {}", e);
            throw new RuntimeException("Error appProduceContollerGetLocation : "+ e.getMessage());
        }
        

        
    }

    
    private void calculationSaldo(TransactionEntity transactionEntity, BankAccountEntity bankAccountEntity, TaskEntity taskEntity){

        bankAccountEntity.setSaldo(bankAccountEntity.getSaldo().subtract(transactionEntity.getTransactionLimit()));
        bankAccountEntity.setChangedBy(transactionEntity.getUser().getUserName());
        bankAccountEntity.setChangedDt(new Date());
        bankAccountRepository.save(bankAccountEntity);
        this.updateTransactionSuccess(transactionEntity);
        this.updateTaskSuccess(taskEntity, transactionEntity.getUser().getUserName());
    }

    private void updateTransactionSuccess(TransactionEntity transactionEntity){
        transactionEntity.setStatus(Constants.SUCCESS);
        transactionEntity.setTransRefresh(true);
        transactionEntity.setMessage("Success");
        transactionEntity.setChangedBy(transactionEntity.getUser().getUserName());
        transactionEntity.setChangedDt(new Date());
        transactionRepository.save(transactionEntity);
    }

    private void updateTaskSuccess(TaskEntity taskEntity, String username){
        taskEntity.setMsg("Success");
        taskEntity.setStatus(Constants.SUCCESS);
        taskEntity.setChangedBy(username);
        taskEntity.setChangedDt(new Date());
        taskRepository.save(taskEntity);
    }

    private void insertLocation(LocationVo locationVo, TransactionEntity transaction, Long taskId){

        log.info("INSERT LOCATION FOR TASK ID : {}", taskId);
        LocationEntity locationEntity = new LocationEntity();
        BeanUtils.copyProperties(locationVo, locationEntity);
        locationEntity.setCity(locationVo.getTimezone());
        locationEntity.setProcessId(transaction.getProcessId());
        locationEntity.setTaskId(taskId);
        locationEntity.setUserId(transaction.getUser().getUserId());
        locationRepository.save(locationEntity);
        
    }
    
}
