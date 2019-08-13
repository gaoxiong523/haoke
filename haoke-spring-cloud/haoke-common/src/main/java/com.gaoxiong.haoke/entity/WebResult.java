package com.gaoxiong.haoke.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaoxiong
 * @ClassName WebResult
 * @Description 前端app响应内容
 * @date 2019/8/13 13:23
 */
@Data
@AllArgsConstructor
public class WebResult {

    @JsonIgnore
    private int status;
    @JsonIgnore
    private String msg;
    @JsonIgnore
    private List<?> list;

    @JsonIgnore
    public static WebResult ok(List<?> list){
        return new WebResult(HttpStatus.OK.value(), "成功", list);
    }
    @JsonIgnore
    public static WebResult ok(List<?> list,String msg){
        return new WebResult(HttpStatus.OK.value(), msg, list);
    }

    public Map<String,Object> getMeta(){
        Map<String, Object> meta = new HashMap<>();
        meta.put("msg",this.msg );
        meta.put("status", this.status);
        return meta;
    }
    public Map<String,Object> getData(){
        Map<String, Object> data = new HashMap<>();
        data.put("list", this.list);
        return data;
    }



}
