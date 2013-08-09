package com.africaapps.league.service.webservice.ssl;

import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrustEverythingTrustManager implements X509TrustManager {
	
	private static Logger logger = LoggerFactory.getLogger(TrustEverythingTrustManager.class);
	
  public java.security.cert.X509Certificate[] getAcceptedIssuers() {
    logger.info("Getting issuers...");
  	return null;
  }

  public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {   }

  public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {   }
}
