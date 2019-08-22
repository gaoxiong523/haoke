package com.gaoxiong.elasticsearch.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author gaoxiong
 * @ClassName House
 * @Description TODO
 * @date 2019/8/22 16:14
 */
@Data
@Document(
        indexName = "house",
        type = "house"
)
@NoArgsConstructor
@AllArgsConstructor
public class House {
    @Id
    private Integer id;
    @Field(
            type = FieldType.Text,
            fielddata = true,
            analyzer = "ik_smart",//建议索引时的分词器
            searchAnalyzer = "ik_max_word" //搜索时的分词器
    )
    private String title;

    private Integer price;
}
