package com.gaoxiong.haoke.im.service;

import com.gaoxiong.haoke.im.pojo.Message;
import com.mongodb.client.result.DeleteResult;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * @author gaoxiong
 * @ClassName MessageService
 * @Description TODO
 * @date 2019/8/15 14:28
 */

public interface MessageService {

    List<Message> findAllByFromAndTo ( Long from, Long to, Integer page, Integer rows );



    Message findById ( String id );

    Message updateMessage( String id, Integer status);

    Message saveMessage ( Message message );


    void deleteMessage ( String id );
}
