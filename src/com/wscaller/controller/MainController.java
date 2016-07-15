package com.wscaller.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wscaller.main.WebServiceUtil;
import com.wscaller.util.JsonUtil;
import com.wscaller.util.ResponseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by dqwork on 2016/7/14.
 */
@Controller
@RequestMapping("/wscaller")
public class MainController {

    @RequestMapping(value = "/getOperations")
    public String getOperations(HttpServletResponse response, String wsdl, ModelMap map){
        List<String> list = WebServiceUtil.getOperations(wsdl);
        String jsonStr = JsonUtil.toJson(list);
        ResponseUtil.sendHtml(response,jsonStr);
        return null;
    }
    @RequestMapping(value = "/getParams")
    public String getParams(HttpServletResponse response, String wsdl, String operation,ModelMap map){
        List<Map<String,String>> list = WebServiceUtil.getParams(wsdl,operation);
        String jsonStr = JsonUtil.toJson(list);
        ResponseUtil.sendHtml(response,jsonStr);
        return null;
    }
    @RequestMapping(value = "/invoke")
    public String invoke(HttpServletResponse response, String wsdl, String operation,String paramStr,ModelMap map){
        try {
            Gson g = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .disableHtmlEscaping()//避免Html特殊字符转为Unicode
                    .serializeNulls()//值为null的key-value也输出
                    .create();
            List<String> params = g.fromJson(paramStr,List.class);
            Object[] objs = WebServiceUtil.invoke(wsdl, operation, params);
            String jsonStr = null;
            if (objs != null && objs.length == 1) {
                jsonStr = objs[0].toString();
            } else {
                jsonStr = JsonUtil.toJson(objs);
            }
            ResponseUtil.sendHtml(response, jsonStr);
        }catch (Exception e){
            ResponseUtil.sendHtml(response, "error");
            e.printStackTrace();
        }
        return null;
    }
}
