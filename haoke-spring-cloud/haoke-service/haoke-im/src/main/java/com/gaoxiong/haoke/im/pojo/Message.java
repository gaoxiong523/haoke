package com.gaoxiong.haoke.im.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * @author gaoxiong
 * @ClassName Message
 * @Description TODO
 * @date 2019/8/15 14:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "message")
@Builder
public class Message {


    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 消息状态
     */
    @Indexed
    private Integer status;

    @Indexed
    @Field("send_date")
    private Date sendDate;

    @Field("read_date")
    private Date readDate;

    /**
     * 发送者
     */
    private User from;

    /**
     * 接受者
     */
    private User to;



}
