package com.gaoxiong.haoke.house.graphqlController.service.impl;

import com.gaoxiong.haoke.entity.map.MapHouseDataResult;
import com.gaoxiong.haoke.entity.map.MapHouseXY;
import com.gaoxiong.haoke.house.graphqlController.service.MyDataFetcher;
import com.gaoxiong.haoke.house.service.MongoHouseService;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gaoxiong
 * @ClassName MapHouseDataFetcher
 * @Description 地图找房 graphql 查找返回数据
 * @date 2019/8/22 0022 下午 8:39
 */
@Component
public class MapHouseDataFetcher implements MyDataFetcher {

    @Autowired
    private MongoHouseService mongoHouseService;

    @Override
    public String fieldName () {
        return "MapHouseData";
    }

    @Override
    public Object dataFetcher ( DataFetchingEnvironment env ) {
        Float lng = ((Double) env.getArgument("lng")).floatValue();
        Float lat = ((Double) env.getArgument("lat")).floatValue();
        Integer zoom = env.getArgument("zoom");
        System.out.println("zoom = " + zoom);
        System.out.println("lat = " + lat);
        System.out.println("lng = " + lng);
//        List<MapHouseXY> list = new ArrayList<>();
//        list.add(new MapHouseXY(116.43244f, 39.929986f));
//        list.add(new MapHouseXY(116.424355f, 39.92982f));
//        list.add(new MapHouseXY(116.423349f, 39.935214f));
//        list.add(new MapHouseXY(116.350444f, 39.931645f));
//        list.add(new MapHouseXY(116.351684f, 39.91867f));
//        list.add(new MapHouseXY(116.353983f, 39.913855f));
//        list.add(new MapHouseXY(116.357253f, 39.923152f));
//        list.add(new MapHouseXY(116.349168f, 39.923152f));
//        list.add(new MapHouseXY(116.354954f, 39.935767f));
//        list.add(new MapHouseXY(116.36232f, 39.938339f));
//        list.add(new MapHouseXY(116.374249f, 39.94625f));
//        list.add(new MapHouseXY(116.380178f, 39.953053f));
        return mongoHouseService.queryHouseData(lng, lat, zoom);
//        return new MapHouseDataResult(list);
    }
}
