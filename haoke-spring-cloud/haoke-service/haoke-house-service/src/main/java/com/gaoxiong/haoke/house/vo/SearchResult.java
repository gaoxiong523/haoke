package com.gaoxiong.haoke.house.vo;

import com.gaoxiong.haoke.house.pojo.HouseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author gaoxiong
 * @ClassName SearchResult
 * @Description TODO
 * @date 2019/8/23 15:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult {
    private Integer totalPage;
    private List<HouseData> list;
}
