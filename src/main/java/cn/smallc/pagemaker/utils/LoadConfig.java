package cn.smallc.pagemaker.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class LoadConfig {

    //读取路径
    private String readPath;
    //筛选百分比
    private int percent;
    //html保存路径
    private String savePath;
    //文件名
    private String fileName;

    /**
     * 构造函数
     *
     * @param fileName     读取的csv文件名，若为日常检查站点可以传空
     */
    public LoadConfig(String fileName) {
        super();
        this.fileName = fileName;
        load();
    }

    private void load() {
        //代码块初始化属性

        //读取properties文件
        Properties pro = new Properties();
        try {
            pro.load(new InputStreamReader(new FileInputStream("E:\\smallC\\workspace\\pagemaker\\src\\main\\resources\\config.properties")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("配置文件加载失败！");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("配置文件加载失败！");
        }

        String readRootPath = pro.getProperty("read.root.path", "utf-8");

        //免审站点
        this.percent = new Integer(pro.getProperty("onesite.percent"));
        this.savePath = pro.getProperty("onesite.save.path");
        this.readPath = readRootPath + fileName;


    }

    public String getReadPath() {
        return readPath;
    }

    public void setReadPath(String readPath) {
        this.readPath = readPath;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }


}
