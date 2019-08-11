package com.gaoxiong.haoke.controller;

import cn.yunlingfly.qiniuspringbootstarter.api.service.IQiniuService;
import com.gaoxiong.haoke.ImageUploadResult;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author gaoxiong
 * @ClassName QiniuController
 * @Description TODO
 * @date 2019/8/10 0010 下午 4:48
 */
@RestController
@RequestMapping("/qiniu")
@Slf4j
public class QiniuController {
    @Autowired
    IQiniuService qiniuService;

    @Value("${qiniu.cdn-prefix}")
    private String cdnPrefix;

    @PostMapping("/image/file")
    public ImageUploadResult uploadImage ( @RequestParam("file") MultipartFile file) throws IOException {
        String key = String.valueOf(System.currentTimeMillis());

        String originalFilename = file.getOriginalFilename();
        File tempFile =null;
        tempFile = File.createTempFile("prefix", "_" + originalFilename);
        file.transferTo(tempFile);

        log.info("临时文件的地址是:==="+tempFile.getAbsolutePath());
        Response response = qiniuService.uploadFile(tempFile, key, false);
        log.info("响应{}",response);
        ImageUploadResult result = new ImageUploadResult();
        result.setName("http://"+cdnPrefix+"/"+key);
        result.setUid(String.valueOf(System.currentTimeMillis()));
        //状态:uploading,done,error,removed
        if (response.statusCode >= 200 && response.statusCode < 300) {
            result.setStatus("done");
        } else {
            result.setStatus("error");
        }
        result.setResponse(response.toString());
        return result;
    }

    @PostMapping("/image/filePath")
    public ImageUploadResult uploadImage(String filePath) throws QiniuException {
        String key = String.valueOf(System.currentTimeMillis());
        Response response = qiniuService.uploadFile(filePath, key, false);
        log.info("响应{}", response);
        ImageUploadResult result = new ImageUploadResult();
        result.setName("http://"+cdnPrefix+"/"+key);
        result.setUid(String.valueOf(System.currentTimeMillis()));
        //状态:uploading,done,error,removed
        if (response.statusCode >= 200 && response.statusCode < 300) {
            result.setStatus("done");
        } else {
            result.setStatus("error");
        }
        result.setResponse(response.toString());
        return result;
    }
}
