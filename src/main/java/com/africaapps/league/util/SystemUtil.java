package com.africaapps.league.util;

public class SystemUtil {

	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	public static final String PATH_SEPARATOR = System.getProperty("path.separator");

	public static final String TMP_DIR = System.getProperty("java.io.tmpdir");
	public static final String USER_HOME_DIR = System.getProperty("user.home");

	public static final String JAVA_CLASSPATH = System.getProperty("java.class.path");
	public static final String OS_NAME = System.getProperty("os.name");

	/** The current operation system. */
	public static final String CURRENT_OS = System.getProperty("os.name");
	/** The name of the Linux OS. */
	protected static final String LINUX_OS = "linux";

	/**
	 * Method to check whether the current operating system is Linux or not.
	 * 
	 * @return The flag indicating if OS is Linux or not.
	 */
	public static final boolean isLinux() {
		if (CURRENT_OS.equalsIgnoreCase(LINUX_OS)) {
			return true;
		} else {
			return false;
		}
	}

	private SystemUtil() {
	}

}
