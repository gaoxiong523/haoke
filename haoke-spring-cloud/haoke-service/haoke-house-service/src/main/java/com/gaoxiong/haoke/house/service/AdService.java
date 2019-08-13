package com.gaoxiong.haoke.house.service;

import com.gaoxiong.haoke.entity.BaseService;
import com.gaoxiong.haoke.entity.WebResult;
import com.gaoxiong.haoke.house.pojo.Ad;

/**
 * @author gaoxiong
 * @ClassName AdService
 * @Description TODO
 * @date 2019/8/13 11:33
 */
public interface AdService extends BaseService<Ad> {

    /**
     * 根据广告类型,分页查询
     * @param type 1,首页轮播广告
     * @param page
     * @param size
     * @return
     */
    WebResult findAllByType ( Integer type, Integer page, Integer size );
}
