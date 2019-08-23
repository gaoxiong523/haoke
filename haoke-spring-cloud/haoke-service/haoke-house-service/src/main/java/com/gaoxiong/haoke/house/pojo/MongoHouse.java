package com.gaoxiong.haoke.house.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author gaoxiong
 * @ClassName MongoHouse
 * @Description TODO
 * @date 2019/8/23 10:20
 */
@Document(collection = "house")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MongoHouse {
    @JsonIgnore
    @Id
    private ObjectId id;

    private Long hid;

    private String title;

    private Float[] loc;
}
