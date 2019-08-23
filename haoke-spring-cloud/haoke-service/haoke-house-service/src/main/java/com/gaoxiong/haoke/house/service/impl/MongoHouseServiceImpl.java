package com.gaoxiong.haoke.house.service.impl;

import com.gaoxiong.haoke.entity.map.MapHouseDataResult;
import com.gaoxiong.haoke.entity.map.MapHouseXY;
import com.gaoxiong.haoke.house.dao.MongoHouseRepository;
import com.gaoxiong.haoke.house.pojo.MongoHouse;
import com.gaoxiong.haoke.house.service.MongoHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.Sphere;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gaoxiong
 * @ClassName MongoHouseServiceImpl
 * @Description TODO
 * @date 2019/8/23 10:24
 */
@Service
public class MongoHouseServiceImpl implements MongoHouseService {
    public static final Map<Integer, Double> BAIDU_ZOOM = new HashMap<>();
    static {
        BAIDU_ZOOM.put(19, 20d / 1000); //单位为km
        BAIDU_ZOOM.put(18, 50d / 1000);
        BAIDU_ZOOM.put(17, 100d / 1000);
        BAIDU_ZOOM.put(16, 200d / 1000);
        BAIDU_ZOOM.put(15, 500d / 1000);
        BAIDU_ZOOM.put(14, 1d);
        BAIDU_ZOOM.put(13, 2d);
        BAIDU_ZOOM.put(12, 5d);
        BAIDU_ZOOM.put(11, 10d);
        BAIDU_ZOOM.put(10, 20d);
        BAIDU_ZOOM.put(9, 25d);
        BAIDU_ZOOM.put(8, 50d);
        BAIDU_ZOOM.put(7, 100d);
        BAIDU_ZOOM.put(6, 200d);
        BAIDU_ZOOM.put(5, 500d);
        BAIDU_ZOOM.put(4, 1000d);
        BAIDU_ZOOM.put(3, 2000d);
        BAIDU_ZOOM.put(2, 5000d);
        BAIDU_ZOOM.put(1, 10000d);
    }
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoHouseRepository mongoHouseRepository;

    @Override
    public MapHouseDataResult queryHouseData ( Float lng, Float lat, Integer zoom ) {

        Point point = new Point(lng, lat);
        Distance distance = new Distance(BAIDU_ZOOM.get(zoom)*1.5, Metrics.KILOMETERS);

        Sphere sphere = new Sphere(point, distance);
        List<MongoHouse> mongoHouseList = mongoHouseRepository.findAllByLocWithin(sphere);
        List<MapHouseXY> xyList = mongoHouseList.stream().map(m -> new MapHouseXY(m.getLoc()[0], m.getLoc()[1])).collect(Collectors.toList());
        return new MapHouseDataResult(xyList);
    }

    @Override
    public Collection<MapHouseXY> queryByTemplate( Float lng, Float lat, Integer zoom){
        double distance = BAIDU_ZOOM.get(zoom) * 1.5 / 111.12; //1.5倍距离范围，根据实际需求调整
        Query query = Query.query(Criteria.where("loc").near(new Point(lng,
                lat)).maxDistance(distance));
        List<MongoHouse> mongoHouses = this.mongoTemplate.find(query, MongoHouse.class);

        List<MapHouseXY> xyList = mongoHouses.stream().map(m -> new MapHouseXY(m.getLoc()[0], m.getLoc()[1])).collect(Collectors.toList());
        return xyList;
    }
}
