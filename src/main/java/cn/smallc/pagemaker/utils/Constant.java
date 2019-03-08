package cn.smallc.pagemaker.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class Constant {

	/**
	 * 记录页面分类情况
	 */
	public static CopyOnWriteArraySet<String> fromSet = new CopyOnWriteArraySet<>();
	
	/**
	 * 页面存放集合，这里面添加新元素的时候fromset中也新增一条记录
	 */
	public static ConcurrentHashMap<String, List<Map<String, String>>> htmlMap = new ConcurrentHashMap<>();
	
	
	/**
	 * 来源匹配检测map，初始化的时候从properties中读取
	 */
	public static HashMap<String, String> sourceCheckMapping = new HashMap<>();

}
