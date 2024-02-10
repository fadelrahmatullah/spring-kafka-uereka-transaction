package com.core.app.entity;

import javax.persistence.*;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_transaction", schema = "tp_tracking")
public class TransactionEntity extends CommonEntity{

	@Id
	@GeneratedValue(generator = "tp_tracking.transaction_ID_GENERATOR", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "tp_tracking.transaction_ID_GENERATOR", sequenceName = "tp_tracking.T_transaction_transaction_ID_SEQ",allocationSize=1)
    @Column(name = "transaction_id")
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private TransactionTypeEntity type;

    @Column(name = "trans_limit", nullable = false)
    private BigDecimal transactionLimit;

    @Column(name = "description")
    private String description;

    @Column(name = "status", nullable = false, columnDefinition = "character varying default 'Pending'")
    private String status;

    @Column(name = "trans_refresh")
    private Boolean transRefresh;

    @Column(name = "process_id")
    private String processId;

    @Column(name = "ip_add")
    private String ipAddress;

    @Column(name = "message")
    private String message;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "try_task")
    private Integer tryTask;
}
