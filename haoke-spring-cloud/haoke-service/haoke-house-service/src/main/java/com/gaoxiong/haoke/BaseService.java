package com.gaoxiong.haoke;

import com.gaoxiong.haoke.pojo.Estate;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * @author gaoxiong
 * @ClassName BaseService
 * @Description TODO
 * @date 2019/8/8 0008 下午 9:02
 */
public interface BaseService<T> {

    public List<T> findAll();


    /**
     * 条件查询+分页
     * @param t
     * @param page
     * @param size
     * @return
     */
    public Page<T> findSearch( T t, int page, int size);


    /**
     * 条件查询
     * @param t
     * @return
     */
    public List<T> findSearch( T t);

    /**
     * 根据ID查询实体
     * @param id
     * @return
     */
    public T findById(Long id);

    /**
     * 增加
     * @param t
     */
    public void add(T t) ;

    /**
     * 修改
     * @param t
     */
    public void update(T t);

    /**
     * 删除
     * @param id
     */
    public void deleteById(Long id);

    Page<T> findAllPage ( int page, int size );
}
