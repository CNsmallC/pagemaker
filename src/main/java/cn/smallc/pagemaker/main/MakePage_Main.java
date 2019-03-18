package cn.smallc.pagemaker.main;

import cn.smallc.pagemaker.entity.PageNews;
import cn.smallc.pagemaker.support.SharedRepositoryFactory;
import cn.smallc.pagemaker.utils.TitleUtils;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author smallC
 * @Date 2019/3/1
 * @Description
 */
public class MakePage_Main {

    public static final String SAVEPATH = "E:/smallC/htmlPage/";


    public static void main(String[] args) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(11,0);
            calendar.set(12,0);
            calendar.set(13,0);
            date = calendar.getTime();

            //开始时间
            Date startTime = sdf.parse("2019-03-17 00:00:00");
            startTime = calendar.getTime();

            calendar.set(11,23);
            calendar.set(12,59);
            calendar.set(13,59);

            //结束时间
            Date endTime = sdf.parse("2019-03-17 23:59:59");
            endTime = calendar.getTime();

            List<PageNews> pageNewsList = SharedRepositoryFactory.getPageRepository()
                    .getByCts(startTime.getTime(), endTime.getTime());


            generateHTML(pageNewsList);

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    public static void generateHTML(List<PageNews> pageNewsList) {

        for (int index = 0;index < pageNewsList.size(); index++){

            PageNews pageNews = pageNewsList.get(index);

            String title = "<h2>" + pageNews.getTitle() + "</h2>";//文章标题

            String cleanTitle;
            String preCleanTitle = "";
            String nextCleanTitle = "";

            cleanTitle = TitleUtils.getCleanTitle(pageNews.getTitle());
            String path = SAVEPATH + pageNews.getCts() + pageNews.getUrlFrom() + "&" + cleanTitle + ".html";

            String changePageJS = "<script type=\"text/javascript\" language=JavaScript charset=\"UTF-8\">      document.onkeydown=function(event){            var e = event || window.event || arguments.callee.caller.arguments[0];            if(e && e.keyCode==37){				document.getElementById(\"pre\").click();              }            if(e && e.keyCode==39){                document.getElementById(\"next\").click();               }                    };</script>";
            if (index!=0){
                preCleanTitle = TitleUtils.getCleanTitle(pageNewsList.get(index-1).getTitle());
                String prePath = SAVEPATH + pageNewsList.get(index-1).getCts()
                        + pageNewsList.get(index-1).getUrlFrom() + "&" + preCleanTitle + ".html";
                changePageJS = changePageJS + "<div><span hidden=\"hidden\"><a href=\""+ prePath +"\" id=\"pre\">上一篇</a></span></div>";
            }if (index!=pageNewsList.size()-1){
                nextCleanTitle = TitleUtils.getCleanTitle(pageNewsList.get(index+1).getTitle());
                String nextPath = SAVEPATH + pageNewsList.get(index+1).getCts()
                        + pageNewsList.get(index+1).getUrlFrom() + "&" + nextCleanTitle + ".html";
                changePageJS = changePageJS +"<div><span hidden=\"hidden\"><a href=\""+ nextPath +"\" id=\"next\">下一篇</a></span></div>";

            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



            String date = sdf.format(pageNews.getCrawlerTime());//获取到文章日期
            if (date == null || date.equals("")) {
                date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//如果没有日期，则用当前日期
            }
            String msg = "<div>" + date + "&nbsp" + pageNews.getSource() + "</div>";
            StringBuilder tempContent = new StringBuilder(pageNews.getContent().replaceFirst("!@#!@", "<p>").replaceAll("!@#!@", "</p><p>"));
            if (tempContent.lastIndexOf("<p>") == -1) {
                return;
            }
            String contentNoImg = tempContent.replace(tempContent.lastIndexOf("<p>"), tempContent.lastIndexOf("<p>") + 3, "").toString();
            //加入图片标签
            Pattern p = Pattern.compile("\\$#imgidx=0{0,3}(\\d+)#\\$");
            Matcher m = p.matcher(contentNoImg);
            while (m.find()) {
//			System.out.println(m.group(0));
//			System.out.println(m.group(1));
                contentNoImg = contentNoImg.replace(m.group(0), "<img src=\"" + pageNews.getIMG().get(Integer.parseInt(m.group(1)) - 1).getSrc() + "\"></img>");
//			contentNoImg = contentNoImg.replace(m.group(0), "<img src=\""+page.getImgs().get(Integer.parseInt(m.group(1))).getSrc()+"\"></img>");
            }
            String htmlString = "<html>" + changePageJS + "<a href=\"" + pageNews.getUrl() + "\" target=_blank>源网站</a>" + title + msg + contentNoImg + "</html>";
//            System.out.println(htmlString);
//            System.out.println(pageNews.getTitle());




            try (BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path)), "utf-8"))) {
                br.write(htmlString);
                br.flush();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }


}
