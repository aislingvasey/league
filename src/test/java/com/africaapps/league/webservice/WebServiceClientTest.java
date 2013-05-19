package com.africaapps.league.webservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.africaapps.league.service.webservice.WebServiceClient;

public class WebServiceClientTest {

	private String url = "file:///home/aisling/development/workspaces/games/league/src/main/resources/local-test-env.wsdl";
	private String username = "ashley.kleynhans@gmail.com";
	private String password = "Mxit_For_AmiscoSA!!";
	
	@Test
	public void checkFeed() throws Exception {
		WebServiceClient client = new WebServiceClient(url, username, password);
		assertTrue(client.isServiceReady());
		assertEquals("[v.1.2.0.15]", client.getServiceVersion());
	}
}
