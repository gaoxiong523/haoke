package com.gaoxiong.haoke.house.controller;

import cn.yunlingfly.qiniuspringbootstarter.api.service.IQiniuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiniu.http.Response;
import org.apache.commons.io.FileUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class QiniuControllerTest {

    @Autowired
    private IQiniuService iQiniuService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @org.junit.Test
    public void uploadImage () throws IOException {
        List<String> lines = FileUtils.readLines(new File("F:\\code\\data.json"), "UTF-8");

        for (String line : lines) {
            String image = MAPPER.readTree(line).get("image").asText();
        Response response = iQiniuService.uploadFile("F:\\code\\images\\" + image, image, false);
        System.out.println(response.toString());
        }


    }

    @org.junit.Test
    public void uploadImage1 () {
    }
}