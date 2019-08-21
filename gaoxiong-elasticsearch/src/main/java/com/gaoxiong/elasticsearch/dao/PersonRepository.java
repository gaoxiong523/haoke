package com.gaoxiong.elasticsearch.dao;

import com.gaoxiong.elasticsearch.pojo.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author gaoxiong
 * @ClassName PersonRepository
 * @Description TODO
 * @date 2019/8/21 15:45
 */
public interface PersonRepository extends ElasticsearchRepository<Person, String> {

}
