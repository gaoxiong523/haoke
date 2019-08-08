package com.gaoxiong.haoke.feignService.house.fallback;

import com.gaoxiong.haoke.entity.Result;
import com.gaoxiong.haoke.entity.StatusCode;
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
            public Result findAllEstate () {
                return new Result(false,StatusCode.ERROR,"调用失败" );
            }

            @Override
            public Result findById ( String id ) {
                return new Result(false,StatusCode.ERROR,"id为"+id+"的楼盘不存在" );
            }

        };
    }
}
