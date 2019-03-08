package cn.smallc.pagemaker.main;

import cn.smallc.pagemaker.utils.pipleline.MyConsolePipeline;
import cn.smallc.pagemaker.utils.pipleline.OnlineHtmlPipleline;
import cn.smallc.pagemaker.utils.Constant;
import cn.smallc.pagemaker.utils.LoadConfig;
import cn.smallc.pagemaker.utils.OnlineHtmlProcessor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

import java.io.*;
import java.util.*;

/**
 * @Author smallC
 * @Date 2019/3/8
 * @Description
 */
public class MakeCheckingPage {

    public static void main(String[] args) {

//    	免审检查
        LoadConfig config = new LoadConfig("2019-03-07_chaihongfang"+".csv");

        String readPath = config.getReadPath();
        String savePath = config.getSavePath();
        //抽取百分比单位%
        Integer checkPercent = config.getPercent();

        List<Map<String, Object>> datas = new ArrayList<>();

        try{
            //读取csv文件内容
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(readPath)),"UTF-8"));
//        	BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(Readpath)),"GBK"));
            String line;
            while ((line=br.readLine()) != null ) {
                if (!",,".equals(line) && !"".equals(line)) {
                    Map<String, Object> map = new HashMap<>();
                    if(line.equals(",,,")){
                        continue;
                    }
                    if(line.split(",").length<4){
                        System.out.println(line);
                    }
                    String url = line.split(",")[1];
                    String originalUrl = line.split(",")[2];
                    String from = line.split(",")[0];
                    String type = line.split(",")[3];
                    //移除进入列表的urlfrom
                    map.put("from", from);
                    map.put("originalUrl", originalUrl);
                    map.put("url", url);
                    map.put("type", type);
                    datas.add(map);
                }
            }

            //先清空html文件夹
            File file = new File(savePath);
            File[] fileArray = file.listFiles();
            System.out.println("确定要删除这"+fileArray.length+"个文件吗？继续请输入：yes");
            Scanner sc = new Scanner(System.in);
            String permit = sc.next();
            sc.close();
            if(!permit.trim().equals("yes")){
                System.out.println("删除失败！");
                return;
            }
            for(File f : fileArray){
                f.delete();
            }

            List<Request> requests = new ArrayList<>();
            for(int i=0;i<datas.size();i++){
                Map<String, Object> map = datas.get(i);
                Request request = new Request(map.get("url").toString());
                request.putExtra("from", map.get("from"));
                request.putExtra("type", map.get("type"));
                request.putExtra("originalUrl", map.get("originalUrl"));
                requests.add(request);
            }
            //爬取并保存页面
            OnlineHtmlProcessor onlineHtmlProcessor = new OnlineHtmlProcessor();
            Spider
                    .create(onlineHtmlProcessor)
                    .addPipeline(new OnlineHtmlPipleline())
                    .addPipeline(new MyConsolePipeline())
                    .startRequest(requests).
                    thread(1000)
                    .run();

            //抽取信息持久化

            //收集信息集合
            List<Map<String,String>> getInfo = new ArrayList<Map<String,String>>();
            int count = 0;//用于计数
            //抽查记录--用于打印
            StringBuffer checkRecord = new StringBuffer();
            for(String key : Constant.fromSet){
                List<Map<String, String>> perList = Constant.htmlMap.get(key);
                //洗牌
                Collections.shuffle(perList);
                //向上取整--抽查个数
                Integer createNum = (int) Math.ceil((perList.size()*((float)checkPercent)/100));
                int num = 1;
                for(Map<String, String> m:perList){
                    if(num <= createNum){
                        String htmlPre = m.get("html");
                        String type = m.get("type");
                        Document doc = Jsoup.parse(htmlPre);
                        //修改页面--存在链接为错的情况
                        if(null!=doc.select("#J_article").first()){
                            //采集信息
                            String articleInfo = doc.select("div[id=title]").text();
                            String content = doc.select("#content").text();
                            String img = doc.select("#content img").toString();
                            //删除js---js请求错误导致页面加载过慢
                            doc.select("script").remove();
                            //修改图片属性
                            img = convertImg(img);

                            //生成文件路径
                            String fileName = key+"_";
                            String eastDayUrl = m.get("url").toString();
                            int lastIndex = eastDayUrl.lastIndexOf("/");
                            if(lastIndex>0 && lastIndex<eastDayUrl.length()){
                                fileName = fileName + eastDayUrl.substring(lastIndex+1, eastDayUrl.length());
                            }else{
                                fileName = fileName + Math.random()*1000+".html";
                            }
                            String filePath = savePath+"/"+fileName;

                            Map<String, String> map = new HashMap<String, String>();
                            map.put("articleInfo", articleInfo);
                            map.put("content", content);
                            map.put("img", img);
                            map.put("fileName", fileName);
                            map.put("originalUrl", m.get("originalUrl").toString());
                            map.put("url", m.get("url").toString());
                            map.put("key", key);
                            map.put("type", type);
                            map.put("doc", doc.toString());
                            map.put("filePath", filePath);
                            getInfo.add(map);
                            count++;
                        }else{
                            System.out.println(m.get("url"));
                            continue;
                        }
                    }else{
                        break;
                    }
                    num ++;
                }
            }

            //按文件名排序
            Collections.sort(getInfo, (o1, o2) -> {
                String string1 = o1.get("fileName");
                String string2 = o2.get("fileName");
                return string1.compareTo(string2);
            });

            //临时字符串，存放当前前文件路径

            for(int i = 0;i<getInfo.size();i++){
                Map<String, String> m = getInfo.get(i);
                String type = m.get("type").toString();
//        		String docStr = "<html><script type=\"text/javascript\" language=JavaScript charset=\"UTF-8\">      document.onkeydown=function(event){            var e = event || window.event || arguments.callee.caller.arguments[0];            if(e && e.keyCode==37){ 				document.getElementById(\"pre\").click();              }            if(e && e.keyCode==39){                document.getElementById(\"next\").click();               }                    }; </script><body><div><span hidden=\"hidden\"><a href=\"\" id=\"pre\">上一篇</a></span><span hidden=\"hidden\"><a href=\"\" id=\"next\">下一篇</a></span></div><table height=\"100%\" width=\"100%\"><tr height=\"5%\"><td><a href=\"\" target=\"_blank\" id=\"eastdayPage\">打开头条页面</a></td><td><a href=\"\" target=\"_blank\" id=\"originalPage\">打开原页面</a> </td></tr><tr height=\"90%\"><td width=\"40%\" ><iframe src=\"\" width=\"100%\" height=\"100%\" id=\"eastday_iframe\" security=\"restricted\" sandbox=\"\"></iframe></td><td width=\"40%\"><iframe src=\"\" width=\"100%\" height=\"100%\" id=\"original_iframe\" security=\"restricted\" sandbox=\"\"></iframe></td></tr><tr height=\"5%\"><td colspan=\"2\" id=\"checkInfo\"> </td></tr></table></body></html>";
                String docStr = "<html><script type=\"text/javascript\" language=JavaScript charset=\"UTF-8\">      document.onkeydown=function(event){            var e = event || window.event || arguments.callee.caller.arguments[0];            if(e && e.keyCode==37){				document.getElementById(\"pre\").click();              }            if(e && e.keyCode==39){                document.getElementById(\"next\").click();               }                    };function copyUrl(){	var Url2=document.getElementById(\"originalUrl\");	Url2.select();	document.execCommand(\"Copy\");	alert(\"已复制好，可贴粘。\");}</script><body><div><span hidden=\"hidden\"><a href=\"\" id=\"pre\">上一篇</a></span><span hidden=\"hidden\"><a href=\"\" id=\"next\">下一篇</a></span></div><table height=\"100%\" width=\"100%\"><tr height=\"5%\"><td><a href=\"\" target=\"_blank\" id=\"eastdayPage\">打开头条页面</a></td><td><a href=\"\" target=\"_blank\" id=\"originalPage\">打开原页面</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"text\" cols=\"20\" rows=\"10\" id=\"originalUrl\"></input><input type=\"button\" onclick=\"copyUrl()\" value=\"点击复制源站url\">  </td></tr><tr height=\"90%\"><td width=\"40%\" ><iframe src=\"\" width=\"100%\" height=\"100%\" id=\"eastday_iframe\" security=\"restricted\" sandbox=\"allow-forms allow-scripts allow-same-origin\"></iframe></td><td width=\"40%\"><iframe src=\"\" width=\"100%\" height=\"100%\" id=\"original_iframe\" security=\"restricted\" sandbox=\"allow-forms allow-scripts allow-same-origin\"></iframe></td></tr><tr height=\"5%\"><td colspan=\"2\" id=\"checkInfo\"> </td></tr></table></body></html>";
                String key = m.get("key");
                //上一个文件路径
                String prePath = "没有了";
                if(i!=0){
                    prePath = getInfo.get(i-1).get("filePath");
                }
                //下一个文件路径
                String currentFilePath = m.get("filePath");
                String nextFilePath = "没有了";
                if(i != getInfo.size()-1){
                    nextFilePath = getInfo.get(i+1).get("filePath");
                }

                Document doc = Jsoup.parse(docStr);
                doc.select("#checkInfo").first().append("<p>文章分类："+type+"</p>");
                doc.select("#eastdayPage").first().attr("href", m.get("url"));
                doc.select("#originalPage").first().attr("href", m.get("originalUrl"));
                doc.select("#pre").first().attr("href", prePath);
                doc.select("#next").first().attr("href", nextFilePath);
                doc.select("#eastday_iframe").first().attr("src", m.get("url"));
                doc.select("#original_iframe").first().attr("src", m.get("originalUrl"));
                doc.select("#originalUrl").first().attr("value", m.get("originalUrl"));

                checkRecord.append(key+"\t"+m.get("originalUrl")+"\t"+m.get("url")+"\t"+type+"\n");
                /**
                 * 生成文件
                 */


                //持久化页面，文件保存为i+".html"
                byte[] bytesHtml = doc.toString().getBytes("utf-8");
                OutputStream os = new FileOutputStream(new File(currentFilePath));
                os.write(bytesHtml);
                os.flush();
                os.close();
            }

            System.out.println("去除404的有效url:"+onlineHtmlProcessor.getTotalCount()+"条");
            System.out.println("检查url:"+count+"条");
            System.out.println("\n");
            System.out.println("抽查明细：");
            System.out.println(checkRecord.toString());



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改图片属性
     * @param img
     * @return
     */
    private static String convertImg(String img) {
        Elements attr = Jsoup.parse(img).select("img").attr("width", "10%");
        return attr.toString();
    }

}
