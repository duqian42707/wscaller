package com.wscaller.interceptor;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by dqwork on 2016/7/14.
 */
public class InfoInterceptor extends AbstractPhaseInterceptor {

    public InfoInterceptor() {
        super(Phase.USER_LOGICAL);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        Object obj = message.getContent(List.class).get(0);
        Class clazz = obj.getClass();
        Method[] methods = clazz.getMethods();
        for(Method m : methods){
            System.out.println(m.getName());
        }
    }
}
