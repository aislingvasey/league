package com.africaapps.league;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import com.africaapps.league.util.DateCalcUtil;
import com.africaapps.league.util.PasswordEncoder;
import com.africaapps.league.util.SystemUtil;

public class DaoTestHelper {
  
  private static final String DEFAULT_PASSWORD = "111111";
	
	/**
	 * Set up the default place holders for replacement dataset.
	 * 
	 * @param replacementDataSet
	 * @return
	 */
	private static ReplacementDataSet createDefaultReplacementDataSet(ReplacementDataSet replacementDataSet) {
		Calendar now = GregorianCalendar.getInstance();
		
		Calendar nowMinus3 = GregorianCalendar.getInstance();
		nowMinus3.add(GregorianCalendar.HOUR, -3);
		
		Calendar nowMinus6 = GregorianCalendar.getInstance();
		nowMinus6.add(GregorianCalendar.HOUR, -6);
		
		Calendar nowMinus5 = GregorianCalendar.getInstance();
		nowMinus5.add(GregorianCalendar.HOUR, -5);
		
		replacementDataSet.addReplacementObject("[NOW]", now.getTime());
		replacementDataSet.addReplacementObject("[NOW - 3]", nowMinus3.getTime());
		replacementDataSet.addReplacementObject("[NOW - 6]", nowMinus6.getTime());
		replacementDataSet.addReplacementObject("[NOW - 5]", nowMinus5.getTime());

    //tomorrow
    Date tomorrow = DateCalcUtil.tomorrow();
    replacementDataSet.addReplacementObject("[tomorrow]", tomorrow);

		//yesterday
    Date yesterday = DateCalcUtil.yesterday();
		replacementDataSet.addReplacementObject("[yesterday]", yesterday);
		
		//today
		Date today = DateCalcUtil.today();
    replacementDataSet.addReplacementObject("[today]", today);
		
		//twoDaysAgo
		Date twoDaysAgo = DateCalcUtil.addDays(today, -2);
		replacementDataSet.addReplacementObject("[twoDaysAgo]", twoDaysAgo);
		
		//Null instances/values
		replacementDataSet.addReplacementObject("[NULL]", null);		
		
		//default password for accessing the web site
		replacementDataSet.addReplacementObject("[DEFAULT_PASSWORD]", PasswordEncoder.getEncodedPassword(DEFAULT_PASSWORD));
		
		//temp directory on local machine
		replacementDataSet.addReplacementObject("[TEMP_DIR]", SystemUtil.TMP_DIR);
				
		return replacementDataSet;
	}
	
	public static ReplacementDataSet initializeReplacementDataSet(File dataSet, Map<String, Object> additionalReplacementSetValues) throws Exception {
		FlatXmlDataSet flatXmlDataSet = new FlatXmlDataSet(dataSet);
		ReplacementDataSet replacementDataSet = new ReplacementDataSet(flatXmlDataSet);
		createDefaultReplacementDataSet(replacementDataSet);
		if (additionalReplacementSetValues != null) {
			for (String key : additionalReplacementSetValues.keySet()) {
				replacementDataSet.addReplacementObject(key, additionalReplacementSetValues.get(key));		
			}
		}
		return replacementDataSet;
	}
}