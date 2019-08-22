package com.gaoxiong.elasticsearch.service;

import com.gaoxiong.elasticsearch.pojo.House;
import com.gaoxiong.elasticsearch.pojo.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private HouseService houseService;

    @org.junit.Test
    public void findAll () {
        List<Person> all = personService.findAll();
        for (Person person : all) {
            System.out.println("person = " + person);
        }
    }

    @Test
    public void save(){
        House house = new House(1001, "整租 南丹大楼 一居室 7500", 7500);
        House save = houseService.save(house);
        System.out.println("save = " + save);


    }

    @Test
    public void saveall(){
        House house = new House(1001, "整租 南丹大楼 一居室 7500", 7500);
        House house2 = new House(1002, "陆家嘴板块，精装设计一室一厅，可拎包入住诚意租。", 8500);
        House house3 = new House(1003, "整租 · 健安坊 1居室 4050", 7500);
        House house4 = new House(1004, "整租 · 中凯城市之光+视野开阔+景色秀丽+拎包入住", 6500);
        House house5 = new House(1005, "整租 · 南京西路品质小区 21213三轨交汇 配套齐* 拎包入住", 6000);
        House house6 = new House(1006, "祥康里 简约风格 *南户型 拎包入住 看房随时", 7000);
        List<House> houses = Arrays.asList(house2, house3, house4, house5, house6,house);
        List<House> houses1 = houseService.saveAll(houses);
        System.out.println("houses1 = " + houses1);


    }

    @Test
    public void search(){
        List<House> list = houseService.searchByKeyWord("南");
        System.out.println("list = " + list);
    }

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void createIndex(){
        // 创建索引，会根据Item类的@Document注解信息来创建
        boolean index = elasticsearchTemplate.createIndex(House.class);
        // 配置映射，会根据Item类中的id、Field等字段来自动完成映射
        boolean b = elasticsearchTemplate.putMapping(House.class);
    }
}