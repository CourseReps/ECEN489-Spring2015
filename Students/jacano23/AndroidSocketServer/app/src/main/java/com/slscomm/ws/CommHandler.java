
package com.slscomm.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Action;

import javax.

/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "CommHandler", targetNamespace = "http://ws.slscomm.com/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface CommHandler {


    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://ws.slscomm.com/CommHandler/getServerResponseRequest", output = "http://ws.slscomm.com/CommHandler/getServerResponseResponse")
    public String getServerResponse(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0);

}
