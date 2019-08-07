package cn.itcast.dubbo.consumer;

import cn.itcast.dubbo.pojo.User;
import cn.itcast.dubbo.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author gaoxiong
 * @ClassName TestUserService
 * @Description TODO
 * @date 2019/8/6 0006 下午 11:40
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestUserService {
//        @Reference(version = "1.0.0")
    @Autowired
    private UserService userService;

    @Test
    public void testQueryAll(){
        List<User> users = userService.queryAll();
        for (User user : users) {
            System.out.println("user = " + user);
        }
    }


}
