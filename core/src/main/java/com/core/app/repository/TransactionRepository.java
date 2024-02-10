package com.core.app.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.core.app.entity.TransactionEntity;

@Component(value = "com.core.app.repository.TransactionRepository")
public interface TransactionRepository extends CrudRepository<TransactionEntity, Long> {
    
    @Query(value = "SELECT t FROM TransactionEntity t WHERE t.processId =:processId ")
    TransactionEntity getsProcessId(@Param("processId") String processId);
}
