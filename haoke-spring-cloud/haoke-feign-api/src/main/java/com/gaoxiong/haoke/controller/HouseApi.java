package com.gaoxiong.haoke.controller;

import com.gaoxiong.haoke.entity.Result;
import com.gaoxiong.haoke.feignService.house.HouseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.GET;

/**
 * @author gaoxiong
 * @ClassName HouseApi
 * @Description TODO
 * @date 2019/8/8 13:37
 */
@RestController
@CrossOrigin
public class HouseApi {

    @Autowired
    private HouseClient houseClient;

    @GetMapping("/estate")
    public Result estateAll(){
        System.out.println("进来了 " + houseClient);
        return houseClient.findAllEstate();
    }

    @GetMapping("/estate/{id}")
    public Result findById( @PathVariable String id ){
        return houseClient.findById(id);
    }


}
