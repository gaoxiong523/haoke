package com.gaoxiong.haoke.house.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author gaoxiong
 * @ClassName HouseData
 * @Description TODO
 * @date 2019/8/23 14:44
 */
@Document(
        indexName = "haoke",
        type = "house",
        shards = 6,
        replicas = 2
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseData {

    @Id
    private String id;
    @Field(type = FieldType.Text,
            analyzer = "ik_max_word",
            searchAnalyzer = "ik_max_word")
    private String title;
    @Field(
            type = FieldType.Keyword,
            index = false
    )
    private String rent;
    @Field(
            type = FieldType.Keyword,
            index = false
    )
    private String floor;
    @Field(
            type = FieldType.Keyword,
            index = false
    )
    private String image;
    @Field(
            type = FieldType.Keyword,
            index = false
    )
    private String orientation;
    @Field(
            type = FieldType.Keyword,
            index = false
    )
    private String houseType;
    @Field(
            type = FieldType.Keyword,
            index = false
    )
    private String rentMethod;
    @Field(
            type = FieldType.Keyword,
            index = false
    )
    private String time;


    private String url;
}
