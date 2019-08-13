package com.gaoxiong.haoke.house.graphqlController.service;

import graphql.schema.DataFetchingEnvironment;

/**
 * @author gaoxiong
 * @ClassName MyDataFetcher
 * @Description TODO
 * @date 2019/8/12 15:01
 */
public interface MyDataFetcher {

    /**
     * 查询的名称
     *
     * @return
     */
    String fieldName ();

    /**
     * 具体实现数据查询的逻辑
     * @param env
     * @return
     */
    Object dataFetcher( DataFetchingEnvironment env );
}
