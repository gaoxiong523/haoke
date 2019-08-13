package com.gaoxiong.haoke.house.service.impl;

import com.gaoxiong.haoke.entity.WebResult;
import com.gaoxiong.haoke.house.dao.AdRepository;
import com.gaoxiong.haoke.house.pojo.Ad;
import com.gaoxiong.haoke.house.service.AdService;
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
 * @ClassName AdServiceImpl
 * @Description TODO
 * @date 2019/8/13 11:33
 */
@Service
@Transactional
public class AdServiceImpl implements AdService {
    @Autowired
    private AdRepository repository;
    @Autowired
    private IdWorker idWorker;

    @Override
    public List<Ad> findAll () {
        return repository.findAll();
    }

    @Override
    public WebResult findAllByType ( Integer type, Integer page, Integer size ) {
        Page<Ad> adPage = repository.findAllByType(type, PageRequest.of(page - 1, size));
        return WebResult.ok(adPage.getContent());
    }

    @Override
    public Page<Ad> findSearch ( Ad ad, int page, int size ) {
        PageRequest pageRequest =  PageRequest.of(page-1, size);
        return repository.findAll(Example.of(ad, exampleMatcher()), pageRequest);
    }

    @Override
    public List<Ad> findSearch ( Ad ad ) {
        return repository.findAll(Example.of(ad, exampleMatcher()));
    }

    @Override
    public Ad findById ( Long id ) {
        return repository.findById(id).get();
    }

    @Override
    public void add ( Ad ad ) {
        ad.setId(idWorker.nextId());
        repository.save(ad);
    }

    @Override
    public void update ( Ad ad ) {
        repository.save(ad);
    }

    @Override
    public void deleteById ( Long id ) {
        repository.deleteById(id);
    }

    @Override
    public Page<Ad> findAllPage ( int page, int size ) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return repository.findAll(pageRequest);
    }

    private ExampleMatcher exampleMatcher (){
        return ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    }
}
