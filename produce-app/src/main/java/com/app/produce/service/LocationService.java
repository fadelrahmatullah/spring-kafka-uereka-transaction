package com.app.produce.service;

import com.core.app.vo.LocationVo;

public interface LocationService {
    
    LocationVo getLocation(String ipAddress);
}
