package com.gaoxiong.haoke.im.service.impl;

import com.gaoxiong.haoke.im.pojo.Message;
import com.gaoxiong.haoke.im.pojo.User;
import com.gaoxiong.haoke.im.repository.MessageRepository;
import com.gaoxiong.haoke.im.service.MessageService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gaoxiong
 * @ClassName MessageServiceImpl
 * @Description TODO
 * @date 2019/8/15 15:04
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<Message> findAllByFromAndTo ( Long from, Long to, Integer page, Integer rows ) {
        return messageRepository.findAllByFrom_IdAndTo_Id(from, to, PageRequest.of(page - 1, rows));
    }

    @Override
    public Message findById ( String id ) {
        ObjectId objectId = new ObjectId(id);
        return messageRepository.findById(objectId).get();
    }

    @Override
    public Message updateMessage ( String id, Integer status ) {
        Message byId = findById(id);
        byId.setStatus(status);
        return messageRepository.save(byId);
    }

    @Override
    public Message saveMessage ( Message message ) {
        return messageRepository.save(message);
    }

    @Override
    public void deleteMessage ( String id ) {
        messageRepository.deleteById(new ObjectId(id));
    }
}
