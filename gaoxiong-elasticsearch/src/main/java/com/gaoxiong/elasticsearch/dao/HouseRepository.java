package com.gaoxiong.elasticsearch.dao;

import com.gaoxiong.elasticsearch.pojo.House;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author gaoxiong
 * @ClassName HouseRepository
 * @Description TODO
 * @date 2019/8/22 16:17
 */
public interface HouseRepository extends ElasticsearchRepository<House, Integer> {

    List<House> findHousesByTitleLike ( String keyWord );

}
