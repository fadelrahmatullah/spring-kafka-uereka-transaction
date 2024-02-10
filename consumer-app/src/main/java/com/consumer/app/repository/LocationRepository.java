package com.consumer.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.consumer.app.entity.LocationEntity;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long>{
    
}
