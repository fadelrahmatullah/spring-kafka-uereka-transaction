package com.app.produce.vo;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountInfoVo {
    
    private String username;
    private String accountNumber;
    private BigDecimal saldo;
}
