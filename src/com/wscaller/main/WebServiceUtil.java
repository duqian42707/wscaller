package com.wscaller.main;

import java.lang.reflect.Field;
import java.util.*;

import javax.wsdl.Operation;
import javax.xml.namespace.QName;

import com.wscaller.interceptor.InfoInterceptor;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.service.model.BindingInfo;
import org.apache.cxf.service.model.BindingMessageInfo;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.service.model.MessagePartInfo;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import com.wscaller.interceptor.ClientInterceptor;

public class WebServiceUtil {
    public static final String OPREATION_ORDER_DEFAULT = "0";
    public static final String OPREATION_ORDER_NAME = "1";

    /**
     * 获取方法列表
     *
     * @param wsdl
     * @return
     */
    public static List<String> getOperations(String wsdl) {
        return getOperations(wsdl, OPREATION_ORDER_DEFAULT);
    }

    /**
     * 获取方法列表
     *
     * @param wsdl
     * @param order 顺序 0默认 1按字母顺序
     * @return
     */
    public static List<String> getOperations(String wsdl, String order) {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(wsdl);
        HTTPClientPolicy policy = new HTTPClientPolicy();
        policy.setConnectionTimeout(10 * 1000);
        policy.setReceiveTimeout(10 * 60 * 1000);
        HTTPConduit conduit = (HTTPConduit) client.getConduit();
        conduit.setClient(policy);
        Endpoint endpoint = client.getEndpoint();
        BindingInfo bindingInfo = endpoint.getEndpointInfo().getBinding();
        List<String> list = new ArrayList();
        for (BindingOperationInfo operationInfo : bindingInfo.getOperations()) {
            String operation = operationInfo.getName().getLocalPart();
            list.add(operation);
        }
        if (OPREATION_ORDER_NAME.equals(order)) {
            Collections.sort(list);
        }
        return list;
    }

    /**
     * 获取方法的参数列表
     *
     * @param wsdl
     * @param operation 顺序 0默认 1按字母顺序
     * @return
     */
    public static List<Map<String, String>> getParams(String wsdl, String operation) {
        try {
            List list = new ArrayList();
            JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
            Client client = dcf.createClient(wsdl);
            HTTPClientPolicy policy = new HTTPClientPolicy();
            policy.setConnectionTimeout(10 * 1000);
            policy.setReceiveTimeout(10 * 60 * 1000);
            HTTPConduit conduit = (HTTPConduit) client.getConduit();
            conduit.setClient(policy);
            Endpoint endpoint = client.getEndpoint();
            QName opName = new QName(endpoint.getService().getName().getNamespaceURI(), operation);
            BindingInfo bindingInfo = endpoint.getEndpointInfo().getBinding();
            if (bindingInfo.getOperation(opName) == null) {
                for (BindingOperationInfo operationInfo : bindingInfo.getOperations()) {
                    if (operation.equals(operationInfo.getName().getLocalPart())) {
                        opName = operationInfo.getName();
                        break;
                    }
                }
            }
            BindingOperationInfo boi = bindingInfo.getOperation(opName);
            BindingMessageInfo inputMessageInfo = boi.getInput();
            List<MessagePartInfo> parts = inputMessageInfo.getMessageParts();
            MessagePartInfo partInfo = parts.get(0);
            Class<?> partClass = partInfo.getTypeClass();
            Field[] fields = partClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Map<String, String> map = new HashMap();
                map.put("FIELD_NAME", fields[i].getName());
                map.put("FIELD_TYPE", fields[i].getType().getName());
                list.add(map);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    /**
     * 请求webservice
     *
     * @param wsdl      url地址
     * @param operation 方法名
     * @param params    参数集合
     * @return
     * @throws Exception
     */
    public static Object[] invoke(String wsdl, String operation, List<String> params) throws Exception {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(wsdl);
//        //添加头信息 以便验证
//		client.getOutInterceptors().add(new ClientInterceptor("admin", "admin"));
        HTTPClientPolicy policy = new HTTPClientPolicy();
        policy.setConnectionTimeout(10 * 1000);
        policy.setReceiveTimeout(10 * 60 * 1000);
        HTTPConduit conduit = (HTTPConduit) client.getConduit();
        conduit.setClient(policy);
        // 下面一段处理 WebService接口和实现类namespace不同的情况
        // CXF动态客户端在处理此问题时，会报No operation was found with the name的异常
        Endpoint endpoint = client.getEndpoint();
        QName opName = new QName(endpoint.getService().getName().getNamespaceURI(), operation);
        BindingInfo bindingInfo = endpoint.getEndpointInfo().getBinding();
        if (bindingInfo.getOperation(opName) == null) {
            for (BindingOperationInfo operationInfo : bindingInfo.getOperations()) {
                if (operation.equals(operationInfo.getName().getLocalPart())) {
                    opName = operationInfo.getName();
                    break;
                }
            }
        }
        //加密请求参数
        if (params == null || params.size() == 0) {
            return client.invoke(opName);
        } else {
            Object[] objs = new Object[params.size()];
            for (int i = 0; i < params.size(); i++) {
//				objs[i]=EncryptUtil.getEncrypt(params.get(i));
                objs[i] = params.get(i);
            }
            return client.invoke(opName, objs);
        }
    }

    /**
     * 请求webservice 返回字符串
     *
     * @param wsdl      url地址
     * @param operation 方法名
     * @param params    参数集合
     * @return 字符串
     * @throws Exception
     */
    public static String invokeForString(String wsdl, String operation, List<String> params) throws Exception {
        Object[] objects = invoke(wsdl, operation, params);
        if (objects != null && objects.length > 0) {
            if (objects[0] instanceof String) {
                return (String) objects[0];
            }
        }
        return null;
    }
}
