package com.gaoxiong.haoke.house.controller;

import com.gaoxiong.haoke.house.service.HouseDataSearchService;
import com.gaoxiong.haoke.house.vo.SearchResult;
import org.elasticsearch.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author gaoxiong
 * @ClassName SearchController
 * @Description 房源搜索
 * @date 2019/8/23 15:25
 */
@RestController
@RequestMapping("/search")
@CrossOrigin
public class SearchController {
    @Autowired
    private HouseDataSearchService searchService;

    @GetMapping
    public SearchResult search(
            @RequestParam("keyWord") String keyWord,
            @RequestParam(value = "page", defaultValue = "1") Integer page
    ){

        if (page > 100) {
            page=1;
        }
      return   searchService.search(keyWord, page);

    }

}
