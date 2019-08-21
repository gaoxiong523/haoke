package com.gaoxiong.elasticsearch.service;

import com.gaoxiong.elasticsearch.dao.PersonRepository;
import com.gaoxiong.elasticsearch.pojo.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author gaoxiong
 * @ClassName PersonService
 * @Description TODO
 * @date 2019/8/21 15:46
 */
@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    List<Person> findAll(){
        List<Person> list = new ArrayList<>();
        Iterable<Person> all = personRepository.findAll();
        Iterator<Person> iterator = all.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }
}
