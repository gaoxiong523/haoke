package com.gaoxiong.haoke.im.repository;

import com.gaoxiong.haoke.im.pojo.Message;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author gaoxiong
 * @ClassName MessageRepository
 * @Description TODO
 * @date 2019/8/15 14:24
 */
@Repository
public interface MessageRepository extends MongoRepository<Message, ObjectId> {


    List<Message> findAllByFrom_IdAndTo_Id( Long fromId, Long toId, Pageable pageable );
}
