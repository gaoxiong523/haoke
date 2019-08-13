package com.gaoxiong.haoke.house.graphqlController.service.impl;

import com.gaoxiong.haoke.entity.IndexAdResult;
import com.gaoxiong.haoke.entity.IndexAdResultData;
import com.gaoxiong.haoke.entity.WebResult;
import com.gaoxiong.haoke.house.graphqlController.service.MyDataFetcher;
import com.gaoxiong.haoke.house.pojo.Ad;
import com.gaoxiong.haoke.house.service.AdService;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gaoxiong
 * @ClassName IndexAdResultDataFetcher
 * @Description TODO
 * @date 2019/8/13 14:08
 */
@Component
public class IndexAdResultDataFetcher implements MyDataFetcher {
    @Autowired
    private AdService adService;
    @Override
    public String fieldName () {
        return "IndexAdList";
    }

    @Override
    public Object dataFetcher ( DataFetchingEnvironment env ) {
        Integer type = env.getArgument("type");
        Integer page = env.getArgument("page");
        Integer size = env.getArgument("size");
        if (type == null) {
            type=1;
        }
        if (page == null) {
            page=1;
        }
        if (size == null) {
            size=5;
        }
        WebResult webResult = adService.findAllByType(type, page, size);
        Map<String, Object> data = webResult.getData();
        List<Ad> list = (List<Ad>) data.get("list");
        IndexAdResult indexAdResult = new IndexAdResult();
        List<IndexAdResultData> dataList = new ArrayList<>();
        for (Ad ad : list) {
            IndexAdResultData resultData = new IndexAdResultData(ad.getUrl());
            dataList.add(resultData);
        }
        indexAdResult.setList(dataList);
        return indexAdResult;
    }
}
