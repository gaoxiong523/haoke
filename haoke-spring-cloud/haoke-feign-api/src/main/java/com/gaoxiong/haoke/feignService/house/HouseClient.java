package com.gaoxiong.haoke.feignService.house;

import com.gaoxiong.haoke.entity.Result;
import com.gaoxiong.haoke.feignService.house.fallback.HouseClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author gaoxiong
 * @ClassName HouseClient
 * @Description TODO
 * @date 2019/8/7 0007 下午 10:36
 */
@FeignClient(name = "HOUSE-SERVICE", fallbackFactory = HouseClientFallbackFactory.class)
public interface HouseClient {


    /**
     * 获取所有楼盘信息
     *
     * @return
     */
    @GetMapping("/estate")
    Result findAllEstate ();

    @GetMapping("/estate/{id}")
    Result findById ( @PathVariable(value = "id") String id );

}
