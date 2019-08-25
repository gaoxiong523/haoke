package com.gaoxiong.haoke.house.service;

import com.gaoxiong.haoke.house.dao.HouseDataRepository;
import com.gaoxiong.haoke.house.pojo.HouseData;
import com.gaoxiong.haoke.house.vo.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.reflect.FieldUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author gaoxiong
 * @ClassName HouseDataSearchService
 * @Description TODO
 * @date 2019/8/23 15:29
 */
@Service
@Slf4j
public class HouseDataSearchService {
    @Autowired
    private HouseDataRepository houseDataRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 关键词高亮显示
     * @param keyWord
     * @param page
     * @return
     */
    public SearchResult search ( String keyWord, Integer page ) {
        Integer size = 10;
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("title", keyWord))
                .withHighlightFields(new HighlightBuilder.Field("title"))
                .withPageable(pageRequest)
                .build();
        AggregatedPage<HouseData> houseData = elasticsearchTemplate.queryForPage(searchQuery, HouseData.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults ( SearchResponse searchResponse, Class<T> aClass, Pageable pageable ) {
                if (searchResponse.getHits().totalHits==0) {
                    return new AggregatedPageImpl<>(Collections.emptyList());
                }
                //获取查询到的数据,然后 进行封装
                List<T> list = new ArrayList<>();
                for (SearchHit searchHit : searchResponse.getHits().getHits()) {
                    T obj = (T) ReflectUtils.newInstance(aClass);
                    Map<String, Object> hitSourceAsMap = searchHit.getSourceAsMap();

                    //写入ID
                    try {
                        FieldUtils.writeField(obj, "id", searchHit.getId(), true);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    //非高亮字段的数据,写入
                    for (Map.Entry<String, Object> entry : hitSourceAsMap.entrySet()) {
                        try {
                            FieldUtils.writeField(obj,entry.getKey() ,entry.getValue() , true);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    for (Map.Entry<String, HighlightField> entry : searchHit.getHighlightFields().entrySet()) {
                        StringBuilder sb = new StringBuilder();
                        Text[] fragments = entry.getValue().getFragments();
                        for (Text fragment : fragments) {
                            sb.append(fragment);
                        }
                        //写入高亮的内容
                        try {
                            FieldUtils.writeField(obj,entry.getKey() ,sb.toString() , true);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    list.add(obj);

                }
                return new AggregatedPageImpl<>(list, pageable,searchResponse.getHits().totalHits );
            }
        });
        log.info(houseData.toString());
        return new SearchResult(houseData.getTotalPages(), houseData.getContent());

    }
}
