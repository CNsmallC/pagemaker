package cn.smallc.pagemaker.utils;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author 刘攀
 * 废弃
 */
public class OnlineHtmlProcessor implements PageProcessor {

    public int totalCount = 0;

    //站点爬取策略
    private Site site = Site.me()
            .setCharset("utf-8")//设置编码
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36")
            .setTimeOut(300000) //设置超时时间
            .setRetryTimes(3) //设置重试次数--单次重试
//					.addCookie(name, value)
//					.setDomain(domain)
//					.addHeader(key, value)
            .setCycleRetryTimes(3)//设置循环重试次数--放置队列尾部循环重试
            .setSleepTime(500)//爬虫爬取间隔
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
            .addHeader("Accept-Encoding", "gzip, deflate")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8")
            .addHeader("Cache-Control", "max-age=0")
            .addHeader("Connection", "keep-alive");

    public Site getSite() {

        return site;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void process(Page page) {
        Document doc = page.getHtml().getDocument();
        String body = doc.toString();

        String url = page.getRequest().getUrl();
        String originalUrl = page.getRequest().getExtra("originalUrl").toString();
        String from = page.getRequest().getExtra("from").toString();
        String type = page.getRequest().getExtra("type").toString();
        if (!body.contains("newstype = '404'")) {
            //过滤元素
            doc.select("#J_article ~ *").remove();

            //修改页面--存在链接为错的情况
            if (null != doc.select("#J_article").first()) {
                //页面信息检测
                String checkInfo = "";

                //检查p标签结构
                Elements pElements = doc.select("div#content > p:not(.section.txt)");
                if (!pElements.isEmpty()) {
                    checkInfo = checkInfo + "正文含有不符合结构的p标签</br>";
                }
                //检查正文标签结构
                Elements pselect = doc.select("div#content > p");
                Elements fselect = doc.select("div#content > figure");
                int psize = 0;
                int fsize = 0;
                if (!pselect.isEmpty()) {
                    psize = pselect.size();
                }

                if (!fselect.isEmpty()) {
                    fsize = fselect.size();
                }
                int asize = doc.select("div#content").first().children().size();
                if (asize != psize + fsize) {
                    checkInfo = checkInfo + "正文含有p/figure以外的标签</br>";
                }

                //检查title长度
                String title = doc.select("div.article-title > h1").text();
                if (title.length() > 30) {
                    checkInfo = checkInfo + "标题长度大于30</br>";
                }
                //检查来源长度以及是否有效
                String source = doc.select("div.article-src-time > span").text();
                if (source.indexOf("来源") != -1) {
                    source = source.substring(source.indexOf("来源"), source.length());
                    if (source.length() > 15) {
                        checkInfo = checkInfo + "来源长度大于15</br>";
                    }
                }

                //检查图片层次结构
                Elements imgelements = doc.select("div#content  img");
                for (Element e : imgelements) {

                    if (!e.parent().tagName().equals("a")) {
                        checkInfo = checkInfo + "图片结构异常</br>";
                        break;
                    }
                    if (!e.parent().parent().tagName().equals("figure")) {
                        checkInfo = checkInfo + "图片结构异常</br>";
                        break;
                    }

                }
                //检查来源是否需要提醒（默认来源需要提醒）
                String sourcestr = doc.select("div.article-src-time").text();
                String defaultSource = Constant.sourceCheckMapping.get(from);
//				if(StringUtils.isEmpty(defaultSource)){
//					checkInfo = checkInfo+"该站点尚未录入默认来源</br>";
//				}else{
//					if(sourcestr.contains(defaultSource)){
//						checkInfo = checkInfo+"文章可能使用默认来源，请注意检查</br>";
//					}
//				}
                if (!StringUtils.isEmpty(defaultSource) && sourcestr.contains(defaultSource)) {
                    checkInfo = checkInfo + "文章可能使用默认来源，请注意检查</br>";
                }

                totalCount++;
                page.putField("checkInfo", checkInfo);
                page.putField("html", doc.toString());
                page.putField("from", from);
                page.putField("url", url);
                page.putField("type", type);
                page.putField("originalUrl", originalUrl);
            }

        }
    }

}
