package com.africaapps.league.exception;

import java.util.ArrayList;
import java.util.List;

import com.africaapps.league.util.SimpleValidatorUtil;
import com.africaapps.league.util.SystemUtil;

public class LeagueValidationException extends Exception {

  private static final long serialVersionUID = 1L;

  protected List<String> errorMessages;
  
  public LeagueValidationException() {
    super();
    this.errorMessages = new ArrayList<String>();
  }
  
  public LeagueValidationException(String errorMessage) {
    super();
    this.errorMessages = new ArrayList<String>();
    this.errorMessages.add(errorMessage);
  }
  
  public void addErrorMessage(String errorMessage) {
    this.errorMessages.add(errorMessage);
  }
  
  public List<String> getErrorMessages() {
    return this.errorMessages;
  }
  
  public int getErrorCount() {
    return errorMessages.size();
  }
  
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("AdminValidationException: ").append(errorMessages.size());
    for(String msg : errorMessages) {
      builder.append(SystemUtil.LINE_SEPARATOR).append(msg);
    }
    return builder.toString();
  }

  public String toSimpleString() {
    List<String> newMessages = new ArrayList<String>();
    for(String msg : errorMessages) {
      if (!newMessages.contains(msg) && SimpleValidatorUtil.isNotNullAndBlank(msg)) {
        newMessages.add(msg);
      }
    }
    
    StringBuilder builder = new StringBuilder();
    for(String msg : newMessages) {
      builder.append(SystemUtil.LINE_SEPARATOR).append(msg);
    }
    return builder.toString();
  }
}
