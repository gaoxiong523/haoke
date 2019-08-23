package com.gaoxiong.haoke.house;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaoxiong.haoke.house.dao.HouseDataRepository;
import com.gaoxiong.haoke.house.pojo.HouseData;
import com.gaoxiong.haoke.house.service.HouseDataSearchService;
import com.gaoxiong.haoke.house.vo.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author gaoxiong
 * @ClassName Test
 * @Description TODO
 * @date 2019/8/23 14:52
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class Test {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private HouseDataRepository houseDataRepository;

    /**
     * 初始化索引
     */
    @org.junit.Test
    public void createIndex () {
        elasticsearchTemplate.createIndex(HouseData.class);

        // 配置映射，会根据Item类中的id、Field等字段来自动完成映射
        boolean b = elasticsearchTemplate.putMapping(HouseData.class);
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @org.junit.Test
    public void insertData () throws IOException {
        List<String> lines = FileUtils.readLines(new File("F:\\code\\data.json"), "UTF-8");
        for (String line : lines) {
        HouseData houseData = MAPPER.readValue(line, HouseData.class);
        log.info(houseData.toString());
        HouseData save = houseDataRepository.save(houseData);
        log.info(save.toString());
        }

    }

    @Autowired
    private HouseDataSearchService houseDataSearchService;

    @org.junit.Test
    public void search(){

        String keyword = "田林十村";

        SearchResult search = houseDataSearchService.search(keyword, 2);
        System.out.println(search.getList().size());
        System.out.println(search.getTotalPage());
    }

}
