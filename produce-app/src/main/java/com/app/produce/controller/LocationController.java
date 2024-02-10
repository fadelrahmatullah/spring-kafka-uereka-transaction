package com.app.produce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.produce.service.LocationService;
import com.core.app.response.Response;
import com.core.app.util.ResponseUtil;
import com.core.app.vo.LocationVo;

@RestController
@RequestMapping("/getLocation")
public class LocationController {
    
    @Autowired
    private ResponseUtil responseUtil;

    @Autowired
    private LocationService locationService;
    
    @PostMapping("/{ipAddress:.+}")
    public Response<LocationVo> create(@PathVariable String ipAddress) {
        LocationVo result = locationService.getLocation(ipAddress);

        if (result == null) {
            responseUtil.generateResponseError("00001", "Data Not Found");
        }

        return responseUtil.generateResponseSuccess(result);
	}

}
