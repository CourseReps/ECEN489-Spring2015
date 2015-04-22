package com.example.joshuacano.androidsocketserver;

import com.slscomm.ws.CommHandler;
import com.slscomm.ws.CommHandlerImplService;

public class Main {

    public static void main(String[] args) {

        CommHandlerImplService helloService = new CommHandlerImplService();
        CommHandler hello = helloService.getCommHandlerImplPort();

        System.out.println(hello.getHelloWorldAsString("15"));
    }
}