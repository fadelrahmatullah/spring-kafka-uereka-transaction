package com.core.app.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.core.app.entity.UserEntity;

@Component(value = "com.core.app.repository.UserRepository")
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    @Query(value = "SELECT t FROM UserEntity t WHERE t.userName =:userName ")
    UserEntity findByUserName(@Param("userName") String userName);
}
