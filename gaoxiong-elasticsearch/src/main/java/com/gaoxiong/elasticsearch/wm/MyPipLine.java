package com.gaoxiong.elasticsearch.wm;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gaoxiong
 * @ClassName MyPipLine
 * @Description TODO
 * @date 2019/8/23 13:26
 */

public class MyPipLine implements Pipeline {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Override
    public void process ( ResultItems resultItems, Task task ) {
        Map<String, Object> data = new HashMap<>();

        data.put("url", resultItems.getRequest().getUrl());
        data.put("title", resultItems.get("title"));//标题
        data.put("rent", resultItems.get("rent"));//租金

        String[] types = StringUtils.split(resultItems.get("type"), ' ');
        data.put("rentMethod", types[0]);//租赁方式
        data.put("houseType", types[1]);//户型，如：2室1厅1卫
        data.put("orientation", types[2]);//朝向
        String[] infos = StringUtils.split(resultItems.get("info"), ' ');
        for (String info : infos) {
            if (StringUtils.startsWith(info, "看房：")) {
                data.put("time", StringUtils.split(info, '：')[1]);
            } else if (StringUtils.startsWith(info, "楼层：")) {
                data.put("floor", StringUtils.split(info, '：')[1]);
            }
        }
        String imageUrl = StringUtils.split(resultItems.get("img"), '"')[3];
        String newName = StringUtils
                .substringBefore(StringUtils
                        .substringAfterLast(resultItems.getRequest().getUrl(),
                                "/"), ".") + ".jpg";
        try {
            this.downloadFile(imageUrl, new File("F:\\code\\images\\" + newName));
            data.put("image", newName);
            String json = MAPPER.writeValueAsString(data);
            FileUtils.write(new File("F:\\code\\data.json"), json + "\n", "UTF-8",
                    true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     *
     * @param url 文件url
     * @param dest 目标目录
     * @throws Exception
     */
    public void downloadFile(String url, File dest) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response =
                HttpClientBuilder.create().build().execute(httpGet);
        try {
            FileUtils.writeByteArrayToFile(dest,
                    IOUtils.toByteArray(response.getEntity().getContent()));
        } finally {
            response.close();
        }
    }
}
