package com.gaoxiong.haoke;

import lombok.Data;

/**
 * @author gaoxiong
 * @ClassName ImageUploadResult
 * @Description 图片上传返回对象, 根据antd要求的返回形式编写
 * @date 2019/8/10 0010 下午 4:58
 */
@Data
public class ImageUploadResult {
    //文件唯一标识
    private String uid;
    //文件名
    private String name;
    //状态:uploading,done,error,removed
    private String status;
    //服务端响应内容
    private String response;
}
