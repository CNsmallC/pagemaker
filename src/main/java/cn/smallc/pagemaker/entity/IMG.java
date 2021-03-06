package cn.smallc.pagemaker.entity;


import cn.smallc.pagemaker.common.ientity.WebAggregateRoot;

public class IMG extends WebAggregateRoot {
    private int ID;
    //所属页面
    private String purl;
    //图片地址
    private String src;
    //位置
    private int idx;
    //代替文字
    private String alt;
    //图片名
    private String pname;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }
}
