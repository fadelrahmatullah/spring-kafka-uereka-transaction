package com.core.app.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import com.core.app.entity.BankAccountEntity;
import com.core.app.entity.BankAccountKey;

@Component(value = "com.core.app.repository.BankAccountRepository")
public interface BankAccountRepository extends CrudRepository<BankAccountEntity, BankAccountKey> {
    
    @Query("SELECT b FROM BankAccountEntity b WHERE b.bankAccountKey.accountNumber = :accountNumber")
    BankAccountEntity getByAccountNumber(@Param("accountNumber") String accountNumber);
}
