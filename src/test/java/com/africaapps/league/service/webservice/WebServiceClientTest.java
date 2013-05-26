package com.africaapps.league.service.webservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.africaapps.league.service.webservice.WebServiceClient;

public class WebServiceClientTest {

	private String url = "file:///home/aisling/development/workspaces/games/league/src/test/resources/local-test-env.wsdl";
	private String username = "";
	private String password = "";
	
	@Test
	public void checkFeed() throws Exception {
		WebServiceClient client = new WebServiceClient(url, username, password);
		assertTrue(client.isServiceReady());
		assertEquals("[v.1.2.0.15]", client.getServiceVersion());
	}
}
