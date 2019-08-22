package com.gaoxiong.elasticsearch.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
    @Field(analyzer = "ik_smart",searchAnalyzer = "ik_max_word",type = FieldType.Keyword,fielddata = true)
    private String hobby;
}
