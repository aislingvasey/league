package com.africaapps.league.service.webservice.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerifyEverythingHostnameVerifier implements HostnameVerifier {

	private static Logger logger = LoggerFactory.getLogger(VerifyEverythingHostnameVerifier.class);
	
  public boolean verify(String string, SSLSession sslSession) {
  	logger.info("Verifying: "+string+" "+sslSession);
      return true;
  }
}