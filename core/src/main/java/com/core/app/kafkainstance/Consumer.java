package com.core.app.kafkainstance;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Consumer {
    
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("accountNumber")
    private String accountNumber;
    @JsonProperty("transLimit")
    private String transLimit;
    @JsonProperty("processId")
    private String processId;
    @JsonProperty("transType")
    private String transType;
}
