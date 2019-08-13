package com.gaoxiong.haoke.house.service.impl;

import com.gaoxiong.haoke.house.dao.EstateDao;
import com.gaoxiong.haoke.house.pojo.Estate;
import com.gaoxiong.haoke.house.service.EstateService;
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
 * @author gaoxiong
 * @ClassName EstateServiceImpl
 * @Description TODO
 * @date 2019/8/8 0008 下午 9:16
 */
@Service(value = "estateService")
@Transactional
public class EstateServiceImpl  implements EstateService {
    @Autowired
    private EstateDao estateDao;



    @Autowired
    private IdWorker  idWorker;

    	/**
	 * 查询全部列表
	 * @return
	 */
	@Override
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
	@Override
    public Page<Estate> findSearch( Estate estate, int page, int size) {
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return estateDao.findAll(Example.of(estate, exampleMatcher()), pageRequest);
	}


	/**
	 * 条件查询
	 * @param estate
	 * @return
	 */
	@Override
    public List<Estate> findSearch( Estate estate) {
		return estateDao.findAll(Example.of(estate, exampleMatcher()));
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	@Override
    public Estate findById( Long id) {
		return estateDao.findById(Long.valueOf(id)).get();
	}

	/**
	 * 增加
	 * @param estate
	 */
	@Override
    public void add( Estate estate) {
		estate.setId( idWorker.nextId() );
		estateDao.save(estate);
	}

	/**
	 * 修改
	 * @param estate
	 */
	@Override
    public void update( Estate estate) {
		estateDao.save(estate);
	}

	/**
	 * 删除
	 * @param id
	 */
	@Override
    public void deleteById( Long id) {
		estateDao.deleteById(Long.valueOf(id));
	}

	@Override
	public Page<Estate> findAllPage ( int page, int size ) {
		return estateDao.findAll(PageRequest.of(page-1,size ));
	}


	private ExampleMatcher exampleMatcher (){
        return ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    }
}
