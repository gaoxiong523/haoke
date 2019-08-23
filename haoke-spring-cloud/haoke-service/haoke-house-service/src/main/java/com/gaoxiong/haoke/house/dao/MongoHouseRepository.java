package com.gaoxiong.haoke.house.dao;

import com.gaoxiong.haoke.house.pojo.MongoHouse;
import com.mongodb.client.model.geojson.Point;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.geo.Sphere;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author gaoxiong
 * @ClassName MongoHouseRepository
 * @Description TODO
 * @date 2019/8/23 10:26
 */
public interface MongoHouseRepository extends MongoRepository<MongoHouse, ObjectId> {

    /**
     * 根据指定球形,搜索范围
     * @param sphere
     * @return
     */
    List<MongoHouse> findAllByLocWithin ( Sphere sphere );
}
