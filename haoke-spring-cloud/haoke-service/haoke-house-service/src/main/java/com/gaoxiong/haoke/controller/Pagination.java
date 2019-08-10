package com.gaoxiong.haoke.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaoxiong
 * @ClassName pa
 * @Description TODO
 * @date 2019/8/9 14:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagination{
    // pagination: {"total":46,"pageSize":10,"current":1}
    private Long total;
    private int pageSize;
    private int current;
}