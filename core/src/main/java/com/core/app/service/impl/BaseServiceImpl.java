package com.core.app.service.impl;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import com.core.app.entity.CommonEntity;
import com.core.app.service.BaseService;

@Service
public class BaseServiceImpl implements BaseService {
    protected void setCreateAuditTrail(CommonEntity entity, String username) {
        entity.setCreatedBy(username);
        entity.setCreatedDt(new Timestamp(System.currentTimeMillis()));
    }

    protected void setUpdateAuditTrail(CommonEntity entity, String username) {
        entity.setChangedBy(username);
        entity.setChangedDt(new Timestamp(System.currentTimeMillis()));
    }

    protected void setDeleteAuditTrail(CommonEntity entity, String username) {
        entity.setChangedBy(username);
        entity.setChangedDt(new Timestamp(System.currentTimeMillis()));
    }

}
