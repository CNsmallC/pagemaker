package cn.smallc.pagemaker.utils;

/**
 * @Author smallC
 * @Date 2019/3/18
 * @Description
 */
public class TitleUtils {

    public static String getCleanTitle(String title){
        title = title.replace("|", "").replace(":", "")
                .replace("【", "").replace("】", "").replace("/", " ")
                .replaceAll("\\\\", " ");

        return title;

    }

}
