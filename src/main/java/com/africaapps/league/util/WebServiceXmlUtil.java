package com.africaapps.league.util;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServiceXmlUtil {

	private static Logger logger = LoggerFactory.getLogger(WebServiceXmlUtil.class);
	
	public static Date getDate(XMLGregorianCalendar xmlCalendar) {
		if (xmlCalendar != null) {
			logger.debug("XmlCalendar: "+xmlCalendar.toString());
			return xmlCalendar.toGregorianCalendar().getTime();
		}
		return null;
	}
	
	
	private WebServiceXmlUtil() {
		//no instances required
	}
}
