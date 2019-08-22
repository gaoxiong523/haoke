package com.gaoxiong.haoke.entity.map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author gaoxiong
 * @ClassName MapHouseDataResult
 * @Description TODO
 * @date 2019/8/22 0022 下午 8:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapHouseDataResult {

    private List<MapHouseXY> list;

}
