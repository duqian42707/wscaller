package com.wscaller.interceptor;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ClientInterceptor extends AbstractPhaseInterceptor<SoapMessage> {

    private String username;
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 创建一个新的实例 ClientLoginInterceptor.
     *
     * @param username
     * @param password
     */
    public ClientInterceptor(String username, String password) {
        super(Phase.PREPARE_SEND);
        this.username = username;
        this.password = password;
    }

    /* (non-Javadoc) 
     * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.message.Message) 
     */
    public void handleMessage(SoapMessage soap) throws Fault {
        List<Header> headers = soap.getHeaders();

        Document doc = DOMUtils.createDocument();

        Element auth = doc.createElement("authrity");
        Element username = doc.createElement("username");
        Element password = doc.createElement("password");

        username.setTextContent(this.username);
        password.setTextContent(this.password);

        auth.appendChild(username);
        auth.appendChild(password);
        //doc.appendChild(auth);  

        headers.add(0, new Header(new QName("tiamaes"), auth));
    }

} 