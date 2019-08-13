package com.gaoxiong.haoke.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

/**
 * @author gaoxiong
 * @ClassName PageInfo
 * @Description
 * @date 2019/8/9 14:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageInfo<T> implements Serializable {
    private List<T> list;
    private Pagination pagination;

    public static PageInfo pageInfo( Page page ){
        List content = page.getContent();
        long totalElements = page.getTotalElements();
        int number = page.getNumber();
        int size = page.getSize();
        return new PageInfo<>(content, new Pagination(totalElements, size, number));
    }
}
