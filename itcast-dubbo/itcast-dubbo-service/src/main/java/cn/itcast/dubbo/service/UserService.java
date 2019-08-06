package cn.itcast.dubbo.service;

import cn.itcast.dubbo.pojo.User;

import java.util.List;

/**
 * @author gaoxiong
 * @ClassName UserService
 * @Description TODO
 * @date 2019/8/6 0006 下午 11:13
 */
public interface UserService {

    List<User> queryAll();
}
