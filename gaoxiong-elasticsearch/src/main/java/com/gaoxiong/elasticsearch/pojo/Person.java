package com.gaoxiong.elasticsearch.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author gaoxiong
 * @ClassName Person
 * @Description TODO
 * @date 2019/8/21 15:42
 */
@Document(
        indexName = "itcast",
        type = "person"
)
@Data
public class Person {

    @Id
    private String id;
    private String name;
    private Integer age;
    private String mail;
    private String hobby;
}
