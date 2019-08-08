package com.gaoxiong.haoke.service;

import java.util.List;

import com.gaoxiong.haoke.util.IdWorker;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import com.gaoxiong.haoke.dao.EstateDao;
import com.gaoxiong.haoke.pojo.Estate;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class EstateService {

	@Autowired
	private EstateDao estateDao;
	
	@Autowired
	private IdWorker idWorker;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Estate> findAll() {
		return estateDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param estate
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Estate> findSearch(Estate estate, int page, int size) {
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return estateDao.findAll(Example.of(estate, exampleMatcher()), pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param estate
	 * @return
	 */
	public List<Estate> findSearch(Estate estate) {
		return estateDao.findAll(Example.of(estate, exampleMatcher()));
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Estate findById(String id) {
		return estateDao.findById(Long.valueOf(id)).get();
	}

	/**
	 * 增加
	 * @param estate
	 */
	public void add(Estate estate) {
		estate.setId( idWorker.nextId() );
		estateDao.save(estate);
	}

	/**
	 * 修改
	 * @param estate
	 */
	public void update(Estate estate) {
		estateDao.save(estate);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		estateDao.deleteById(Long.valueOf(id));
	}

	

 private ExampleMatcher exampleMatcher (){
        return ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    }

}
