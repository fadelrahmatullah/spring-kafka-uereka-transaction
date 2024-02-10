package com.app.produce.service.impl;

import java.math.BigDecimal;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.app.produce.service.TransactionService;
import com.app.produce.utils.IpUtil;
import com.app.produce.vo.TransactionResponse;
import com.app.produce.vo.TransactionVo;
import com.core.app.constants.Constants;
import com.core.app.entity.BankAccountEntity;
import com.core.app.entity.TransactionEntity;
import com.core.app.entity.TransactionTypeEntity;
import com.core.app.entity.UserEntity;
import com.core.app.exception.ValidationException;
import com.core.app.kafkainstance.Consumer;
import com.core.app.kafkainstance.KafkaInstance;
import com.core.app.repository.BankAccountRepository;
import com.core.app.repository.TransactionRepository;
import com.core.app.repository.TransactionTypeRepository;
import com.core.app.repository.UserRepository;
import com.core.app.service.impl.BaseServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class TransactionServiceImpl extends BaseServiceImpl implements TransactionService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaInstance kafkaInstance;

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Override
    public TransactionResponse transaction(TransactionVo reqVo) {

        TransactionEntity transaction = new TransactionEntity();

        UserEntity user = userRepository.findByUserName(reqVo.getUsername());
        if (user == null) {
            throw new ValidationException("000012", "User Not Found");
        }

        TransactionTypeEntity transactionTypeEntity = transactionTypeRepository.findByType(reqVo.getTransType());
        if (transactionTypeEntity == null) {
            throw new ValidationException("000012", "Type Transaction Not Found");
        }

        BankAccountEntity bankAccountEntity = bankAccountRepository.getByAccountNumber(reqVo.getAccountNumber()); 
        if (bankAccountEntity == null) {
            throw new ValidationException("000012", "Account Not Found");
        }

        this.setCreateAuditTrail(transaction, reqVo.getUsername());
        transaction.setUser(user);
        transaction.setType(transactionTypeEntity);
        transaction.setDescription(reqVo.getDescription());
        transaction.setTransactionLimit(new BigDecimal(reqVo.getTransLimit()));
        transaction.setAccountNumber(reqVo.getAccountNumber());

        transaction.setProcessId((String.valueOf(new Random().nextInt(9000000) + 1000000)));
        transaction.setStatus(Constants.START);
        transaction.setIpAddress(this.getIp());
        transaction.setTransRefresh(false);

        transactionRepository.save(transaction);

        this.produceMsg(reqVo,transaction.getProcessId());
        
        TransactionResponse response = new TransactionResponse();
        BeanUtils.copyProperties(transaction, response);
        return response;
    }

    private void produceMsg(TransactionVo transactionVo, String processId){

        ObjectMapper objectMapper = new ObjectMapper();
        Consumer consumer = new Consumer();
        consumer.setAccountNumber(transactionVo.getAccountNumber());
        consumer.setTransLimit(new BigDecimal(transactionVo.getTransLimit()).toString());
        consumer.setUserName(transactionVo.getUsername());
        consumer.setProcessId(processId);
        consumer.setTransType(transactionVo.getTransType());

        String json;
        try {
            json = objectMapper.writeValueAsString(consumer);
            log.info("Send Msg : {}",json);
            kafkaInstance.produce("topic-transaction-01", json);
        } catch (Exception e) {
            throw new ValidationException("000012", e.getMessage());
        }
    }

    @Override
    public String getIp() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
            .getRequest();

        String xForwardedForHeader = IpUtil.getIpPublicServiceAws();

        if (xForwardedForHeader == null) {
            xForwardedForHeader = request.getRemoteAddr() == null ? IpUtil.getIpAddress(request) : null;
        }

        return xForwardedForHeader;
    }


    
}
