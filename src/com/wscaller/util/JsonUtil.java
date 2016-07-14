package com.wscaller.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;

public class JsonUtil {
	
	/**
	 * object转为json
	 * @param data
	 */
	public static String toJson(Object data) {
		Gson g = new GsonBuilder()
		.setDateFormat("yyyy-MM-dd HH:mm:ss")
		.disableHtmlEscaping()//避免Html特殊字符转为Unicode
		.serializeNulls()//值为null的key-value也输出
		.create();
		return g.toJson(data);
	}
	/**
	 * json转为object
	 * 注意：慎用！该方法会将数字类型的值转为Double型
	 * @param data
	 */
	public static Object toObject(String data) {
		Gson g = new GsonBuilder()
		.setDateFormat("yyyy-MM-dd HH:mm:ss")
		.disableHtmlEscaping()//避免Html特殊字符转为Unicode
		.serializeNulls()//值为null的key-value也输出
		.create();
		return g.fromJson(data, Object.class);
	}
	public static void main(String[] args) {
		String a = "{\"id\":43}";
		System.out.println(toObject(a));
	}
	
	public static void main2(String[] args) {
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "to<m");
		map.put("age", 18);
		map.put("hobby", null);
		map.put("birthday", new Date());
		list.add(map);
		Gson g = new GsonBuilder()
//	     .enableComplexMapKeySerialization()
//	     .setDateFormat(DateFormat.LONG)
//	     .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
//	     .setPrettyPrinting()
//	     .setVersion(1.0)
		 .serializeNulls()
		 .setDateFormat("yyyy-MM-dd HH:mm:ss")
		 .disableHtmlEscaping()
	     .create();
		String jsonData = g.toJson(list);
		System.out.println("jsonData:"+jsonData);
	}
}
