package cn.smallc.pagemaker.main;

import cn.smallc.pagemaker.entity.PageNews;
import cn.smallc.pagemaker.support.SharedRepositoryFactory;
import org.springframework.data.domain.Page;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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


    public static void main(String[] args) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            //开始时间
            Date startTime = sdf.parse("2019-03-04 00:00:00");
            //结束时间
            Date endTime = sdf.parse("2019-03-05 00:00:00");

            List<PageNews> pageNewsList = SharedRepositoryFactory.getPageRepository()
                    .getByCts(startTime.getTime(), endTime.getTime());


            for (PageNews pageNews : pageNewsList) {
                generateHTML(pageNews);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    public static void generateHTML(PageNews pageNews) {
        String title = "<h2>" + pageNews.getTitle() + "</h2>";//文章标题
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
        String htmlString = "<html>" + "<a href=\"" + pageNews.getUrl() + "\" target=_blank>源网站</a>" + title + msg + contentNoImg + "</html>";
        System.out.println(htmlString);
        System.out.println(pageNews.getTitle());
        String title1;

        title1 = pageNews.getTitle().trim().replace("|", "").replace(":", "")
                .replace("【", "").replace("】", "").replace("/", " ");
        title1 = title1.replaceAll("\\\\", " ");

//        String path = "E:/smallC/htmlPage/"+pageNews.getUrlFrom()+"&"+pageNews.getCts()+".html";
        String path = "E:/smallC/htmlPage/" + pageNews.getUrlFrom() + "&" + title1 + ".html";
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
