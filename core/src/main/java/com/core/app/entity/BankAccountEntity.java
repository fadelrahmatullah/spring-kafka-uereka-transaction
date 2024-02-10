package com.core.app.entity;


import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "tp_tracking", name = "t_bank_account")
public class BankAccountEntity extends CommonEntity{
    
    @EmbeddedId
    private BankAccountKey bankAccountKey;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;
	
	@Column(name = "bank_name")
	private String bankName;

    @Column(name = "saldo")
	private BigDecimal saldo;

}
