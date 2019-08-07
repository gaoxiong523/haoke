package cn.itcast.dubbo.service.impl;

import cn.itcast.dubbo.pojo.User;
import cn.itcast.dubbo.service.UserService;
import com.alibaba.dubbo.config.annotation.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gaoxiong
 * @ClassName UserServiceImpl
 * @Description TODO
 * @date 2019/8/6 0006 下午 11:14
 */
@Service(version = "${dubbo.service.version}")//声明这是dubbo服务
@org.springframework.stereotype.Service
public class UserServiceImpl implements UserService {
    /**
     * 查询所有用户,模拟实现
     * @return
     */
    @Override
    public List<User> queryAll () {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new User(Long.valueOf(i + 1), "username_" + i, "123456", 10 + i));
        }
        return list;
    }
}
