package com.gaoxiong.haoke.feignService.house;

import com.gaoxiong.haoke.feignService.house.fallback.HouseClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author gaoxiong
 * @ClassName HouseClient
 * @Description TODO
 * @date 2019/8/7 0007 下午 10:36
 */
@FeignClient(value = "house-service", fallbackFactory = HouseClientFallbackFactory.class)
public interface HouseClient {

    /**
     * 获取房源列表
     * @return
     */
    @GetMapping("/hello")
    ResponseEntity houses ();

}
