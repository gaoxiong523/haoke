package com.gaoxiong.elasticsearch.wm;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * @author gaoxiong
 * @ClassName LianjiaPageProcessor
 * @Description TODO
 * @date 2019/8/23 11:55
 */

public class LianjiaPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(200);

    @Override
    public void process ( Page page ) {
        Html html = page.getHtml();
        page.addTargetRequests(html.css(".content__list--item--title a").links().all());
                page.putField("title", html.xpath("//div[@class='content clear w1150']/p/text()").toString());
        page.putField("rent", html.xpath("//p[@class='content__aside--title']/span/text()").toString());
        page.putField("type",html.xpath("//p[@class='content__article__table']/allText()").toString());
        page.putField("info", html.xpath("//div[@class='content__article__info']/allText()").toString());
        page.putField("img", html.xpath("//div[@class='content__article__slide__item']/img").toString());
        if (page.getResultItems().get("title") == null) {
            page.setSkip(true);
//分页
            for (int i = 1; i <= 100; i++) {
                page.addTargetRequest("https://sh.lianjia.com/zufang/pg" + i);
            }
        }
    }

    @Override
    public Site getSite () {
        return this.site;
    }

    public static void main ( String[] args ) {
        Spider.create(new LianjiaPageProcessor())
                .addUrl("https://sh.lianjia.com/zufang/")
                .thread(10)
                .addPipeline(new MyPipLine())
                .start();
    }
}
