package com.gaoxiong.haoke.house.dao;

import com.gaoxiong.haoke.house.pojo.Ad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdRepository extends JpaRepository<Ad, Long>, JpaSpecificationExecutor<Ad> {

    Page<Ad> findAllByType ( Integer type, Pageable pageable );
}