package com.gaoxiong.elasticsearch.service;

import com.gaoxiong.elasticsearch.pojo.Person;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @org.junit.Test
    public void findAll () {
        List<Person> all = personService.findAll();
        for (Person person : all) {
            System.out.println("person = " + person);
        }
    }
}