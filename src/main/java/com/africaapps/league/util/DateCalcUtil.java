package com.africaapps.league.util;

import java.util.Date;
import java.util.GregorianCalendar;

public class DateCalcUtil {

  public static final long ONE_MINUTE = 60 * 1000;  
  public static final long ONE_HOUR = ONE_MINUTE * 60;  
  public static final long ONE_DAY = ONE_HOUR * 24;
  public static final long ONE_WEEK = ONE_DAY * 7;
  
  /**
   * @return The same time yesterday
   */
  public static Date oneDayAgo() {
    return new Date(System.currentTimeMillis() - ONE_DAY);
  }
  
  /**
   * @return Yesterday 00:00:00.000
   */
  public static Date yesterday() {
    GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance();
    cal.add(GregorianCalendar.DAY_OF_YEAR, -1);
  
    cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
    cal.set(GregorianCalendar.MINUTE, 0);
    cal.set(GregorianCalendar.SECOND, 0);
    cal.set(GregorianCalendar.MILLISECOND, 0);
    return cal.getTime();
  }
  
  /**
   * @return Tomorrow 00:00:00.000
   */
  public static Date tomorrow() {
    GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance();
    cal.add(GregorianCalendar.DAY_OF_YEAR, 1);
  
    cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
    cal.set(GregorianCalendar.MINUTE, 0);
    cal.set(GregorianCalendar.SECOND, 0);
    cal.set(GregorianCalendar.MILLISECOND, 0);
    return cal.getTime();
  }
  
  /**
   * @return Today 00:00:00.000
   */
  public static Date today() {
    GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance();
  
    cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
    cal.set(GregorianCalendar.MINUTE, 0);
    cal.set(GregorianCalendar.SECOND, 0);
    cal.set(GregorianCalendar.MILLISECOND, 0);
    return cal.getTime();
  }

  public static Date getClosestDay() {
    GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance();
    if(cal.get(GregorianCalendar.HOUR_OF_DAY) < 12) {
      cal.add(GregorianCalendar.DAY_OF_YEAR, -1);
    }
    cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
    cal.set(GregorianCalendar.MINUTE, 0);
    cal.set(GregorianCalendar.SECOND, 0);
    cal.set(GregorianCalendar.MILLISECOND, 0);
    
    return cal.getTime();
  }
  
  public static Date addDays(Date theDate, int nrDays) {
    return new Date(theDate.getTime() + (nrDays * ONE_DAY));
  }
  
  public static Date clearHHMMSS(Date date) {
    GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance();
    cal.setTime(date);
    cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
    cal.set(GregorianCalendar.MINUTE, 0);
    cal.set(GregorianCalendar.SECOND, 0);
    cal.set(GregorianCalendar.MILLISECOND, 0);
    return cal.getTime();	  
  }
}
