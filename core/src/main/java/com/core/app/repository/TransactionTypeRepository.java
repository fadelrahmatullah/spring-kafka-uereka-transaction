package com.core.app.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import com.core.app.entity.TransactionTypeEntity;

@Component(value = "com.core.app.repository.TransactionTypeRepository")
public interface TransactionTypeRepository extends CrudRepository<TransactionTypeEntity, Long> {

    @Query(value = "SELECT t FROM TransactionTypeEntity t WHERE t.typeName =:typeName ")
    TransactionTypeEntity findByType(@Param("typeName") String typeName);
}
