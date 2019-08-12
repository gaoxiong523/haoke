package com.gaoxiong.haoke.graphqlController.service.impl;

import com.gaoxiong.haoke.graphqlController.service.MyDataFetcher;
import com.gaoxiong.haoke.service.HouseResourcesService;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author gaoxiong
 * @ClassName HouseResourcePageImpl
 * @Description TODO
 * @date 2019/8/12 15:16
 */
@Component
public class HouseResourcesPageImpl implements MyDataFetcher {

    @Autowired
    private HouseResourcesService houseResourcesService;

    @Override
    public String fieldName () {
        return "HouseResourcesPage";
    }

    @Override
    public Object dataFetcher ( DataFetchingEnvironment env ) {
        Integer page = env.getArgument("page");
        if (page == null) {
            page = 1;
        }
        Integer size = env.getArgument("size");
        if (size == null) {
            size = 5;
        }
        return this.houseResourcesService.findAllPage( page, size);
    }
}
