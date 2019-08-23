package com.gaoxiong.haoke.house.service;

import com.gaoxiong.haoke.house.dao.HouseDataRepository;
import com.gaoxiong.haoke.house.pojo.HouseData;
import com.gaoxiong.haoke.house.vo.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        AggregatedPage<HouseData> houseData = elasticsearchTemplate.queryForPage(searchQuery, HouseData.class);
        log.info(houseData.toString());
        return new SearchResult(houseData.getTotalPages(), houseData.getContent());

    }
}
