package com.gaoxiong.haoke.house.controller;

import com.gaoxiong.haoke.entity.WebResult;
import com.gaoxiong.haoke.house.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author gaoxiong
 * @ClassName AdController
 * @Description TODO
 * @date 2019/8/13 12:00
 */
@RestController
@RequestMapping("/ad")
@CrossOrigin
public class AdController {
    @Autowired
    private AdService adService;

    @GetMapping
    public WebResult findAll( @RequestParam(value = "type",defaultValue = "1") Integer type,
                              @RequestParam(value = "page",defaultValue = "1") Integer page,
                              @RequestParam(value = "size",defaultValue = "5") Integer size){
        return adService.findAllByType(type, page, size);
    }

}
