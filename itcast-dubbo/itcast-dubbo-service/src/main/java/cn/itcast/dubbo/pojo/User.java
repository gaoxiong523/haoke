package cn.itcast.dubbo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author gaoxiong
 * @ClassName User
 * @Description TODO
 * @date 2019/8/6 0006 下午 11:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1278302205118528507L;
    private Long id;
    private String username;
    private String password;
    private Integer age;

}
