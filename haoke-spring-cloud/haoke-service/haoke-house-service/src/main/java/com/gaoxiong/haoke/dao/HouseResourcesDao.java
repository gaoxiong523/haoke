package com.gaoxiong.haoke.dao;

import org.springframework.data.jpa.repository.JpaRepository;


import com.gaoxiong.haoke.pojo.HouseResources;
/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface HouseResourcesDao extends JpaRepository<HouseResources,Long>{
	
}
