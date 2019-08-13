package com.gaoxiong.haoke.house.graphqlController.service.impl;

import com.gaoxiong.haoke.house.graphqlController.service.MyDataFetcher;
import com.gaoxiong.haoke.house.service.HouseResourcesService;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author gaoxiong
 * @ClassName MyDataFetcherImpl
 * @Description TODO
 * @date 2019/8/12 15:04
 */
@Component
public class MyDataFetcherImpl implements MyDataFetcher {

    @Autowired
    private HouseResourcesService houseResourcesService;

    @Override
    public String fieldName () {
        return "HouseResources";
    }

    @Override
    public Object dataFetcher ( DataFetchingEnvironment env ) {
        Long id = env.getArgument("id");
        return this.houseResourcesService.findById(id);
    }
}
