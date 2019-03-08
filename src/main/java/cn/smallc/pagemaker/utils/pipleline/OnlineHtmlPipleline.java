package cn.smallc.pagemaker.utils.pipleline;

import cn.smallc.pagemaker.utils.Constant;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 废弃
 * @author 刘攀
 *
 */
public class OnlineHtmlPipleline implements Pipeline {

	public void process(ResultItems results, Task task) {

		if(null == results.get("from") ||null == results.get("html") ||null == results.get("url")){
			return;
		}
		String from = results.get("from").toString();
		String html = results.get("html").toString();
		String url = results.get("url").toString();
		String originalUrl = results.get("originalUrl").toString();
		String type = results.get("type").toString();
		String checkInfo = results.get("checkInfo").toString();
		if(Constant.fromSet.contains(from)){
			Map<String, String> infoMap = new HashMap<String, String>();
			infoMap.put("html", html);
			infoMap.put("url", url);
			infoMap.put("originalUrl", originalUrl);
			infoMap.put("type", type);
			infoMap.put("checkInfo", checkInfo);
			Constant.htmlMap.get(from).add(infoMap);
		}else{
			Constant.fromSet.add(from);
			ArrayList<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
			Map<String, String> infoMap = new HashMap<String, String>();
			infoMap.put("html", html);
			infoMap.put("url", url);
			infoMap.put("originalUrl", originalUrl);
			infoMap.put("type", type);
			infoMap.put("checkInfo", checkInfo);
			arrayList.add(infoMap);
			Constant.htmlMap.put(from, arrayList);
		}
	}

}
