package com.gaoxiong.haoke.dao;

import org.springframework.data.jpa.repository.JpaRepository;


import com.gaoxiong.haoke.pojo.Estate;
/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface EstateDao extends JpaRepository<Estate,Long>{
	
}
