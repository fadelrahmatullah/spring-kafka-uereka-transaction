package com.app.produce.service;

import com.app.produce.vo.TransactionResponse;
import com.app.produce.vo.TransactionVo;
import com.core.app.service.BaseService;

public interface TransactionService extends BaseService {
    
    TransactionResponse transaction(TransactionVo reqVo);
    String getIp();
}
