package com.consumer.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.consumer.app.entity.TaskEntity;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long>{
    
    @Query(value = "SELECT t FROM TaskEntity t WHERE t.status =:status ")
    List<TaskEntity> getTaskByStatus(@Param("status") String status);

    @Query("SELECT t FROM TaskEntity t LEFT JOIN LocationEntity l ON t.taskId = l.taskId WHERE l.taskId IS NULL and t.status ='Success' ")
    List<TaskEntity> getTasksWithoutLocation();

}
