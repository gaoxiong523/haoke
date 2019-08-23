package com.gaoxiong.haoke.house.service;

import com.gaoxiong.haoke.entity.map.MapHouseDataResult;
import com.gaoxiong.haoke.entity.map.MapHouseXY;

import java.util.Collection;

/**
 * @author gaoxiong
 * @ClassName MongoHouseService
 * @Description TODO
 * @date 2019/8/23 10:23
 */

public interface MongoHouseService {

    public MapHouseDataResult queryHouseData ( Float lng, Float lat, Integer zoom );

    public Collection<MapHouseXY> queryByTemplate ( Float lng, Float lat, Integer zoom );
}
