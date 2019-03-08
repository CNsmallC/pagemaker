package cn.smallc.pagemaker.utils.pipleline;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class MyConsolePipeline implements Pipeline{

	public void process(ResultItems resultItems, Task task) {
		System.out.println("下载页面："+resultItems.getRequest().getUrl());
	}

	
	
}
