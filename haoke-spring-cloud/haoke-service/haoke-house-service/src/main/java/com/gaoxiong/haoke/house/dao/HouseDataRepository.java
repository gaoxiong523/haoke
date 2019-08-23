package com.gaoxiong.haoke.house.dao;

import com.gaoxiong.haoke.house.pojo.HouseData;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author gaoxiong
 * @ClassName HouseDataRepository
 * @Description TODO
 * @date 2019/8/23 14:57
 */
public interface HouseDataRepository extends ElasticsearchRepository<HouseData,String> {


    List<HouseData> findAllByTitleContains ( String title, HighlightBuilder.Field field );



}
