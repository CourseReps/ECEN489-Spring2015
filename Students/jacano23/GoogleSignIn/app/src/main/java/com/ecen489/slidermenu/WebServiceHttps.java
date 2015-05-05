package com.ecen489.slidermenu;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpsTransportSE;

public class WebServiceHttps {
    //Namespace of the Webservice - can be found in WSDL
    private static String NAMESPACE = "http://ws.slscomm.com/";
    //SOAP Action URI again Namespace + Web method name
    private static String SOAP_ACTION = "http://ws.slscomm.com/CommHandler/";

    //private static final String HOST = "10.202.126.130";
    private static final String HOST = "elcloudo.tamu.edu";
    private static final int PORT = 8443;
    //private static final int PORT = 8080;
    private static final String FILE = "/SLSComm/commhandler?wsdl";
    private static final int TIMEOUT = 1000;

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

        SSLConnection.allowAllSSL();

        HttpsTransportSE androidHttpsTransport = new HttpsTransportSE(HOST, PORT, FILE, TIMEOUT);


        try {
            // Invoke web service
            androidHttpsTransport.call(SOAP_ACTION+webMethName, envelope);
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
