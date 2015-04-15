package com.slscomm.ws;

import javax.jws.WebService;

//Service Implementation Bean

@WebService(endpointInterface = "com.slscomm.ws.CommHandler")
public class CommHandlerImpl implements CommHandler{

	@Override
	public int getHelloWorldAsString(int x) {
		return x*2;
	}
}

