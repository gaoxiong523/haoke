package com.gaoxiong.haoke.im.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaoxiong
 * @ClassName User
 * @Description TODO
 * @date 2019/8/15 14:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    private Long id;

    private String username;

}
