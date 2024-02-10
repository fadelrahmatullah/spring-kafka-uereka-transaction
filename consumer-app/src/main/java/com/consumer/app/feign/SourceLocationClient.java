package com.consumer.app.feign;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
// import org.springframework.cloud.netflix.feign.EnableFeignClients;
// import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.core.app.response.Response;
// import com.core.app.vo.LocationVo;
import com.core.app.vo.LocationVo;

@FeignClient(name="produce-app")
@EnableFeignClients
public interface SourceLocationClient {

    @GetMapping("api/v1/produce/getIp")
    public Response<String> appProduceContollerGetIp();

    @PostMapping("api/v1/getLocation/{ipAddress:.+}")
    public Response<LocationVo> appProduceContollerGetLocation(@PathVariable String ipAddress);
}
