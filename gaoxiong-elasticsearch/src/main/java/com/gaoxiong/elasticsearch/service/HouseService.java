package com.gaoxiong.elasticsearch.service;

import com.gaoxiong.elasticsearch.dao.HouseRepository;
import com.gaoxiong.elasticsearch.pojo.House;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author gaoxiong
 * @ClassName HouseService
 * @Description TODO
 * @date 2019/8/22 16:17
 */
@Service
public class HouseService {
    @Autowired
    private HouseRepository houseRepository;

    public House save( House house ){
        House save = houseRepository.save(house);
        return save;
    }

    public List<House> saveAll( List<House> houses ){
        Iterable<House> houses1 = houseRepository.saveAll(houses);
        Iterator<House> iterator = houses1.iterator();
        List<House> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    List<House> searchByKeyWord(String keyWord){
       return houseRepository.findHousesByTitleLike(keyWord);
    }
}
