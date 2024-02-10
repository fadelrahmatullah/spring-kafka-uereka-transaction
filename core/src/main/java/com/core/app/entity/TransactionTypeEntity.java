package com.core.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_transaction_type", schema = "tp_tracking")
public class TransactionTypeEntity extends CommonEntity{
    
	@Id
	@GeneratedValue(generator = "TYPE_ID_GENERATOR", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "TYPE_ID_GENERATOR", sequenceName = "T_TRANSACTION_TYPE_TYPE_ID_SEQ",allocationSize=1)
    @Column(name = "type_id")
    private Long typeId;

    @Column(name = "type_name", nullable = false)
    private String typeName;

    public TransactionTypeEntity(String typeName) {
        this.typeName = typeName;
    }

    
}
