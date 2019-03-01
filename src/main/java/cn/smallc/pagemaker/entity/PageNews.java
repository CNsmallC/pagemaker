package cn.smallc.pagemaker.entity;

import cn.smallc.pagemaker.common.ientity.WebAggregateRoot;

import java.util.Date;
import java.util.List;

public class PageNews extends WebAggregateRoot {
    private int ID;
    private String url;
    private String title;
    private String category;
    private String secondCategory;
    private String urlFrom;
    private String keywords;
    private String author;
    //文章时间
    private String timeStr;
    private String content;
    private List<IMG> IMG;
    private String source;
    private Date crawlerTime;
    private long cts;



    public Date getCrawlerTime() {
        return crawlerTime;
    }

    public void setCrawlerTime(Date crawlerTime) {
        this.crawlerTime = crawlerTime;
    }

    public String getSecondCategory() {
        return secondCategory;
    }

    public void setSecondCategory(String secondCategory) {
        this.secondCategory = secondCategory;
    }

    public long getCts() {
        return cts;
    }

    public void setCts(long cts) {
        this.cts = cts;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrlFrom() {
        return urlFrom;
    }

    public void setUrlFrom(String urlFrom) {
        this.urlFrom = urlFrom;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<IMG> getIMG() {
        return IMG;
    }

    public void setIMG(List<IMG> IMG) {
        this.IMG = IMG;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
