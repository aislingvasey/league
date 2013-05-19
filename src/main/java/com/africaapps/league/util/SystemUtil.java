package com.africaapps.league.util;

public class SystemUtil {

  public static final String LINE_SEPARATOR = System.getProperty("line.separator");
  public static final String FILE_SEPARATOR = System.getProperty("file.separator");
  public static final String PATH_SEPARATOR = System.getProperty("path.separator");
  
  public static final String TMP_DIR = System.getProperty("java.io.tmpdir");
  public static final String USER_HOME_DIR = System.getProperty("user.home");
  
  public static final String JAVA_CLASSPATH = System.getProperty("java.class.path");  
  public static final String OS_NAME = System.getProperty("os.name");
  
  private SystemUtil() {}
  
}
