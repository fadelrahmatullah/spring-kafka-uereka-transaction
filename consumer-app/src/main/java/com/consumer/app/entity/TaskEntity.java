package com.consumer.app.entity;

import javax.persistence.*;

import com.core.app.entity.CommonEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_task_bank", schema = "tp_tracking")
public class TaskEntity extends CommonEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "transaction_id", unique = true)
    private Long transactionId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "msg", nullable = false)
    private String msg;
}

