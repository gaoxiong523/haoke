package com.gaoxiong.haoke.im.service.impl;

import com.gaoxiong.haoke.im.pojo.Message;
import com.gaoxiong.haoke.im.pojo.UserData;
import com.gaoxiong.haoke.im.service.MessageService;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceImplTest {

    @Autowired
    private MessageService messageService;

    @Test
    public void findAllByFromAndTo () {
        Long from = 1001L;
        Long to = 1002L;
        Integer page =1;
        Integer rows = 5;
        List<Message> messageList = messageService.findAllByFromAndTo(from, to, page, rows);
        for (Message message : messageList) {
            System.out.println("message = " + message);
            System.out.println(message.getId().toString());
            System.out.println(message.getId().toHexString());
        }
        System.out.println(" = =================================");
    }

    @Test
    public void findById () {
        String id = "5d550cefb58704193c7b45e4";
        Message message = messageService.findById(id);
        System.out.println("message = " + message);
        System.out.println(" = =================================");
    }

    @Test
    public void updateMessage () {
        String id = "5d550cefb58704193c7b45e4";
        Message message = messageService.updateMessage(id, 1);
        System.out.println("message = " + message);

        System.out.println(" = =================================");
    }

    @Test
    public void saveMessage () {

        for (int i = 0; i < 10; i++) {
            ObjectId id = new ObjectId();
            Message message = Message.builder()
                    .id(id)
                    .from(UserData.USER_MAP.get(1001L))
                    .to(UserData.USER_MAP.get(1002L))
                    .msg("测试消息"+i)
                    .status(0)
                    .sendDate(new Date())
                    .build();
            messageService.saveMessage(message);
        }

        System.out.println(" = =================================");
    }

    @Test
    public void deleteMessage () {
        String id = "5d550cefb58704193c7b45e4";
        messageService.deleteMessage(id);
        System.out.println(" = =================================");
    }
}