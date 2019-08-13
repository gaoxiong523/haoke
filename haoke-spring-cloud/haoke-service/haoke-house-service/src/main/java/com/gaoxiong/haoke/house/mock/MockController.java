package com.gaoxiong.haoke.house.mock;

import com.gaoxiong.haoke.house.config.MockConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gaoxiong
 * @ClassName MockController
 * @Description TODO
 * @date 2019/8/13 0013 下午 10:35
 */
@RestController
@RequestMapping("/mock")
public class MockController {
    @Autowired
    private MockConfig mockConfig;

    /**
     * 菜单
     *
     * @return
     */
    @GetMapping("/index/menu")
    public String indexMenu () {
        return this.mockConfig.getIndexMenu();
    }

    /**
     * 首页资讯
     *
     * @return
     */
    @GetMapping("/index/info")
    public String indexInfo () {
        return this.mockConfig.getIndexInfo();
    }

    /**
     * 首页 问答
     *
     * @return
     */
    @GetMapping("/index/faq")
    public String indexFaq () {
        return this.mockConfig.getIndexFaq();
    }

    /**
     * 首页房源信息
     *
     * @return
     */
    @GetMapping("/index/house")
    public String indexHouse () {
        return this.mockConfig.getIndexHouse();
    }

    /**
     * 查询资讯
     *
     * @return
     */
    @GetMapping("/infos/list")
    public String infosList ( @RequestParam Integer type ) {
        switch (type) {
            case 1:
                return this.mockConfig.getInfosList1();
            case 2:
                return this.mockConfig.getInfosList2();
            case 3:
                return this.mockConfig.getInfosList3();
            default:
                return this.mockConfig.getInfosList1();
        }
    }

    /**
     * 我的中心
     *
     * @return
     */
    @GetMapping("/my/info")
    public String myInfo () {
        return this.mockConfig.getMy();
    }
}
