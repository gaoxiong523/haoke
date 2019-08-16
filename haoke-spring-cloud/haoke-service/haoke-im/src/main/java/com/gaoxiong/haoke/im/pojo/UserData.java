package com.gaoxiong.haoke.im.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gaoxiong
 * @ClassName UserData
 * @Description TODO
 * @date 2019/8/15 14:20
 */
public class UserData {

    public static final Map<Long, User> USER_MAP = new HashMap<>();

    static {
        USER_MAP.put(1001L, User.builder().id(1001L).username("张三1").build());
        USER_MAP.put(1002L, User.builder().id(1002L).username("张三2").build());
        USER_MAP.put(1003L, User.builder().id(1003L).username("张三3").build());
        USER_MAP.put(1004L, User.builder().id(1004L).username("张三4").build());
    }
}
