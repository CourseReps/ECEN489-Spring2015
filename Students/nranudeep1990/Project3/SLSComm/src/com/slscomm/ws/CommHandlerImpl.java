package com.slscomm.ws;

import javax.jws.WebService;

//Service Implementation Bean

@WebService(endpointInterface = "com.slscomm.ws.CommHandler")
public class CommHandlerImpl implements CommHandler{

	@Override
	public String getServerResponse(String query) {
		ProcessRequest proRequest = new ProcessRequest();
		return proRequest.process(query);
	}
}

