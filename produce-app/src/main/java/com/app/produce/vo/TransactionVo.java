package com.app.produce.vo;

import javax.validation.constraints.NotNull;

import javax.validation.constraints.NotBlank;


import lombok.Data;

@Data
public class TransactionVo {
    
    @NotBlank(message = "accountNumber Not Null")
    private String accountNumber;
    @NotBlank(message = "username Not Null")
    private String username;
    @NotBlank(message = "transactionType Not Null")
    private String transType;
    @NotNull(message = "transLimit Not Null")
    private Float transLimit;
    private String description;
}
