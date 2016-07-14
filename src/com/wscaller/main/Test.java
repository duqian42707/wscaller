package com.wscaller.main;

import com.google.gson.Gson;
import com.wscaller.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        String wsdl = "http://192.168.1.5:8081/yanfamanage/services/webservice?wsdl";
        List params = new ArrayList<>();
        params.add("dqcorp60");
        params.add("DARM_GGB");
        try {
//            List list = WebServiceUtil.getOperations(wsdl,"1");
            List list2 = WebServiceUtil.getParams(wsdl,"donate");
            System.out.println(list2);
            Object[] objs = null;
//            objs = WebServiceUtil.invoke(wsdl, "getCorpAuth", params);
            String jsonStr = null;
            if (objs != null && objs.length == 1) {
                jsonStr = JsonUtil.toJson(objs[0]);
            } else {
                jsonStr = JsonUtil.toJson(objs);
            }
            System.out.println(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
