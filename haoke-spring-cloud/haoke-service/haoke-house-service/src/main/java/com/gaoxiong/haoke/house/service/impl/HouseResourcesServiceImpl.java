package com.gaoxiong.haoke.house.service.impl;

import com.gaoxiong.haoke.house.dao.HouseResourcesDao;
import com.gaoxiong.haoke.house.pojo.HouseResources;
import com.gaoxiong.haoke.house.service.HouseResourcesService;
import com.gaoxiong.haoke.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class HouseResourcesServiceImpl implements HouseResourcesService {

	@Autowired
	private HouseResourcesDao houseResourcesDao;
	
	@Autowired
	private IdWorker idWorker;

	/**
	 * 查询全部列表
	 * @return
	 */
	@Override
	public List<HouseResources> findAll() {
		return houseResourcesDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param houseResources
	 * @param page
	 * @param size
	 * @return
	 */
	@Override
	public Page<HouseResources> findSearch( HouseResources houseResources, int page, int size) {
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return houseResourcesDao.findAll(Example.of(houseResources, exampleMatcher()), pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param houseResources
	 * @return
	 */
	@Override
	public List<HouseResources> findSearch( HouseResources houseResources) {
		return houseResourcesDao.findAll(Example.of(houseResources, exampleMatcher()));
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	@Override
	public HouseResources findById( Long id) {
		return houseResourcesDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param houseResources
	 */
	@Override
	public void add( HouseResources houseResources) {
		houseResources.setId( idWorker.nextId() );
		houseResourcesDao.save(houseResources);
	}

	/**
	 * 修改
	 * @param houseResources
	 */
	@Override
	public void update( HouseResources houseResources) {
		houseResourcesDao.save(houseResources);
	}

	/**
	 * 删除
	 * @param id
	 */
	@Override
	public void deleteById( Long id) {
		houseResourcesDao.deleteById(id);
	}

	@Override
	public Page<HouseResources> findAllPage ( int page, int size ) {
		PageRequest pageRequest = PageRequest.of(page - 1, size);
		return houseResourcesDao.findAll(pageRequest);
	}


	private ExampleMatcher exampleMatcher (){
        return ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    }

}
