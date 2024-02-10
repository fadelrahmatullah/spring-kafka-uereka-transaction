package com.consumer.app.service.impl;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.consumer.app.entity.TaskEntity;
import com.consumer.app.repository.TaskRepository;
import com.consumer.app.service.ConsumerValidationService;
import com.core.app.constants.Constants;
import com.core.app.entity.BankAccountEntity;
import com.core.app.entity.TransactionEntity;
import com.core.app.kafkainstance.Consumer;
import com.core.app.repository.BankAccountRepository;
import com.core.app.repository.TransactionRepository;
import com.core.app.service.impl.BaseServiceImpl;

@Service
@Transactional
public class ConsumerValidationServiceImpl extends BaseServiceImpl implements ConsumerValidationService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private TaskRepository taskRepository;

    
    final BigDecimal minimumRemainder = new BigDecimal("20000.00");

    @Override
    public void validationConsumer(Consumer consumer) {
       
        TransactionEntity transactionEntity = transactionRepository.getsProcessId(consumer.getProcessId());
        BankAccountEntity bankAccountEntity = bankAccountRepository.getByAccountNumber(consumer.getAccountNumber());
    
        // check trans refresh
        
        if (transactionEntity.getTransRefresh()) {
            this.failedValidation(transactionEntity, "Transaksi Sudah Di lakukan untuk process "+transactionEntity.getProcessId());
        }

        // check status
        if (!transactionEntity.getStatus().equals(Constants.START)) {
            this.failedValidation(transactionEntity, "Transaksi Sudah Di lakukan untuk process "+transactionEntity.getProcessId());
        }
       
        // check saldo
        BigDecimal saldo = bankAccountEntity.getSaldo();
        BigDecimal transLimit = transactionEntity.getTransactionLimit();
        if (transLimit.compareTo(saldo) < 0) {

            BigDecimal difference = saldo.subtract(transLimit);

            //check minumum saldo 
            if (difference.compareTo(minimumRemainder) <= 0) {
                this.failedValidation(transactionEntity, "Saldo Tidak CUKUP");
            }

        }else{
            this.failedValidation(transactionEntity, "Saldo Tidak CUKUP");
        }
        
        this.successValidation(transactionEntity);
    }

    @Modifying
    private void successValidation(TransactionEntity transactionEntity){
        this.setUpdateAuditTrail(transactionEntity, transactionEntity.getUser().getUserName());
        transactionEntity.setStatus(Constants.PROCESS);
        transactionRepository.save(transactionEntity);
        this.inserTask(transactionEntity);
    }
    
    @Modifying
    private void failedValidation(TransactionEntity transactionEntity, String msg){
        this.setUpdateAuditTrail(transactionEntity, transactionEntity.getUser().getUserName());
        transactionEntity.setStatus(Constants.FAILED);
        transactionEntity.setMessage(msg);
        transactionRepository.save(transactionEntity);
        this.inserTask(transactionEntity);
    }

    private void inserTask(TransactionEntity transactionEntity){

        TaskEntity task = new TaskEntity();
        task.setMsg(transactionEntity.getMessage() == null ? "Running" : transactionEntity.getMessage());
        task.setStatus(transactionEntity.getStatus());
        task.setTransactionId(transactionEntity.getTransactionId());
        task.setUserId(transactionEntity.getUser().getUserId());
        
        this.setCreateAuditTrail(task, transactionEntity.getUser().getUserName());
        taskRepository.save(task);
    }
}
