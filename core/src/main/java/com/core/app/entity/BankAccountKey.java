package com.core.app.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountKey implements Serializable {

    @GeneratedValue(generator = "ACCOUNT_ID_GENERATOR", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ACCOUNT_ID_GENERATOR", sequenceName = "T_BANK_ACCOUNT_ACCOUNT_ID_SEQ",allocationSize=1)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "account_number")
    private String accountNumber;

}
