package com.app.produce.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.app.produce.service.TransactionService;
import com.app.produce.vo.TransactionResponse;
import com.app.produce.vo.TransactionVo;
import com.core.app.response.Response;
import com.core.app.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "produce/")
public class ProduceController {



    @Autowired
    private ResponseUtil responseUtil;

    @Autowired
    private TransactionService transactionService;
    

    @GetMapping("hello-world")
    public Response<String> helloWorld() {
        log.debug("Hello World !!!");

        return responseUtil.generateResponseSuccess("Hello World !!!");
    }
 
    @PostMapping("producemsg")
    public Response<TransactionResponse> create(@RequestBody @Valid TransactionVo transactionVo) {

        TransactionResponse response = transactionService.transaction(transactionVo);
        
        return responseUtil.generateResponseSuccess(response);
	}

    @GetMapping(value = "getIp", produces = "application/json;charset=UTF-8")
    public Response<String> getIp() {
        return responseUtil.generateResponseSuccess(transactionService.getIp());
    }

}
