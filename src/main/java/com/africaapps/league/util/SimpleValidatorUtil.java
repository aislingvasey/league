package com.africaapps.league.util;

import java.io.File;
import java.util.Date;

public class SimpleValidatorUtil {
	  
	  private static final String NUMERIC_REG_EXP = "^-?[0-9]*";  
	  public static final String IP_ADDRESS_REG_EXP =
	    "^((0|1[0-9]{0,2}|2[0-9]{0,1}|2[0-4][0-9]|25[0-5]|[3-9][0-9]{0,1})\\.){3}(0|1[0-9]{0,2}|2[0-9]{0,1}|2[0-4][0-9]|25[0-5]|[3-9][0-9]{0,1})$";
	  public static final String MAC_ADDRESS_REG_EXP = "((\\d|([a-f]|[A-F])){2}:){5}(\\d|([a-f]|[A-F])){2}";  
	  	  
	  public static boolean isNotNull(Object value) {
	    if (value == null) {
	      return false;
	    } else {
	      return true;
	    }
	  }
	  
	  public static boolean isNotNullAndEmpty(Object[] value) {
	    if (value == null || value.length == 0) {
	      return false;
	    } else {
	      return true;
	    }
	  }

	  public static boolean isNotNullAndBlank(String value) {
	    if (value == null || value.trim().equals("")) {
	      return false;
	    } else {
	      return true;
	    }
	  }
	  
	  public static boolean isValidPositiveNumeric(Long id) {
	    if (id != null && id > 0) {
	      return true;
	    } else {
	      return false;
	    }
	  }

	  public static boolean isValidDirectory(String name) {
	    if (isNotNullAndBlank(name)) {
	      File file = new File(name);
	      if (file.exists() && file.isDirectory()) {
	        return true;
	      }
	    }
	    return false;
	  }

	  public static boolean isValidFile(File file) {
	    if (isNotNull(file) && file.exists() && file.isFile()) {
	      return true;
	    }
	    return false;
	  }

	  public static boolean isPositiveInteger(String value) {
	    if (isNotNullAndBlank(value)) {
	      try {
	        Integer theInteger = Integer.valueOf(value);
	        if (theInteger > 0) {
	          return true;
	        } else {
	          return false;
	        }
	      } catch (NumberFormatException e) {
	        return false;
	      }
	    }
	    return false;
	  }

	  public static boolean isValidInput(byte[] input) {
	    if (input != null && input.length > 0) {
	      return true;
	    } else {
	      return false;
	    }
	  }
	  
	  public static boolean isValidStartEndDates(Date start, Date end) {
	    if (isNotNull(start) && isNotNull(end)) {
	      if (start.getTime() < end.getTime()) {
	        return true;
	      }
	    }
	    return false;
	  }
	  
	  public static boolean isValidIpAddress(String value) {
	    if (isNotNullAndBlank(value) && value.matches(IP_ADDRESS_REG_EXP)) {
	      return true;
	    } else {
	      return false;
	    }
	  }
	  
	  public static boolean isValidMacAddress(String value) {
	    if (isNotNullAndBlank(value) && value.matches(MAC_ADDRESS_REG_EXP)) {
	      return true;
	    } else {
	      return false;
	    }
	  }
	  
	  public static boolean isAttributeDifferent(Object o1, Object o2) {
	    if (o1 == null && o2 == null) {
	      return false;
	    } else if ((o1 == null && o2 != null)
	        || (o1 != null && o2 == null)
	        || (!o1.equals(o2))) {
	      return true;
	    } else {
	      return false;
	    }
	  }
	  
	  public static boolean isValidNumeric(String value) {
	    if (isNotNullAndBlank(value) && value.matches(NUMERIC_REG_EXP)) {
	      return true;
	    } else {
	      return false;
	    }
	  }

	  private SimpleValidatorUtil() {
	    // no instances required
	  }
	}
