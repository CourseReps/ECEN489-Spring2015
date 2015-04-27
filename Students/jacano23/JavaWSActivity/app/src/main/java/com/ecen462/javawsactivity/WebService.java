package com.ecen462.javawsactivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;

/*
class AlternativeWebService {
    private static final String SOAP_ACTION = "http://ws.chathura.com/getCategoryObject";
    private static final String METHOD_NAME = "getCategoryObject";
    private static final String NAMESPACE = "http://ws.chathura.com";
    private static final String NAMESPACE2 = "http://ws.chathura.com/xsd";
    private static final String HOST = "myURL.de";
    private static final int PORT = 8443;
    private static final String FILE = "/WebProject_KomplexeObjekte_SSL/services/HelloWorldWS?wsdl";
    private static final int TIMEOUT = 1000;
    public static String invokeStringWS(String name, String WebMethName) {

        String resTxt = null;

        SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

        HttpsTransportSE androidHttpsTransport = new HttpsTransportSE(HOST, PORT, FILE, TIMEOUT);

        return resTxt;
    }
}
*/


public class WebService {
    //Namespace of the Webservice - can be found in WSDL
    private static String NAMESPACE = "http://ws.slscomm.com/";
    //Webservice URL - WSDL File location
    private static String URL = "http://elcloudo.tamu.edu:8080/SLSComm/commhandler?wsdl";
    //SOAP Action URI again Namespace + Web method name
    private static String SOAP_ACTION = "http://ws.slscomm.com/CommHandler/";

    public static String invokeHelloWorldWS(String name, String webMethName) {

        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo sayHelloPI = new PropertyInfo();
        // Set Name
        sayHelloPI.setName("arg0");
        // Set Value
        sayHelloPI.setValue(name);
        // Set dataType
        sayHelloPI.setType(String.class);
        // Add the property to request object
        request.addProperty(sayHelloPI);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        // Set output SOAP object

        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);


        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to resTxt variable static variable
            resTxt = response.toString();

        } catch (Exception e) {
            //Print error
            e.printStackTrace();
            //Assign error message to resTxt
            resTxt = "Error occurred";
        }
        //Return resTxt to calling object
        return resTxt;
    }
}
