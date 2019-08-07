package com.gaoxiong.haoke.feignService.house.fallback;

import com.gaoxiong.haoke.feignService.house.HouseClient;
import feign.hystrix.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author gaoxiong
 * @ClassName HouseFallbackFactory
 * @Description 调用失败的时候返回
 * @date 2019/8/7 0007 下午 10:38
 */
@Component
public class HouseClientFallbackFactory implements FallbackFactory<HouseClient> {
    @Override
    public HouseClient create ( Throwable throwable ) {
        return new HouseClient() {
            @Override
            public ResponseEntity houses () {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
            }

        };
    }
}
