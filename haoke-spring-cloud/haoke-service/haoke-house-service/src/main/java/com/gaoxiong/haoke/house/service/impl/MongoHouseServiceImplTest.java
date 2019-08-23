package com.gaoxiong.haoke.house.service.impl;

import com.gaoxiong.haoke.entity.map.MapHouseDataResult;
import com.gaoxiong.haoke.entity.map.MapHouseXY;
import com.gaoxiong.haoke.house.service.MongoHouseService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class MongoHouseServiceImplTest {
    Float lng = 121.48130241985999f;
    Float lat = 31.235156971414239f;
    Integer zoom = 12;
    @Autowired
    private MongoHouseService mongoHouseService;

    @org.junit.Test
    public void queryHouseData () {
        MapHouseDataResult mapHouseDataResult = mongoHouseService.queryHouseData(lng, lat, zoom);
        System.out.println("mapHouseDataResult = " + mapHouseDataResult);
        System.out.println("mapHouseDataResult = " + mapHouseDataResult.getList().size());

    }

    @org.junit.Test
    public void queryByTemplate () {
        Collection<MapHouseXY> mapHouseXIES = mongoHouseService.queryByTemplate(lng, lat, zoom);
        System.out.println("mapHouseXIES = " + mapHouseXIES);
        System.out.println("mapHouseXIES = " + mapHouseXIES.size());
    }
}