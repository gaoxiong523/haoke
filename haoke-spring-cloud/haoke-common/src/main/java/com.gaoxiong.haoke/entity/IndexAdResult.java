package com.gaoxiong.haoke.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author gaoxiong
 * @ClassName IndexAdResult
 * @Description TODO
 * @date 2019/8/13 14:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexAdResult {
    private List<IndexAdResultData> list;
}
