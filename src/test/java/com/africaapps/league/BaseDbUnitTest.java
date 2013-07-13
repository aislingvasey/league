package com.africaapps.league;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.dbunit.DatabaseTestCase;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.africaapps.league.util.DateCalcUtil;
import com.africaapps.league.util.SystemUtil;

/**
 * The base class for all unit tests that use Spring-managed dao classes. This unit test setups up and tears down the 
 * required test data for the unit test using DbUnit. Each sub-class can overwrite the getTestDataFilename() method
 * to return the test data file's name. Additionally, sub-classes can also override getReplacementDataSet() in order 
 * to add additional replacement values for the DbUnit source data file.
 * @author Aisling Vasey
 * @version $Id$
 */
public abstract class BaseDbUnitTest extends DatabaseTestCase {

  /** The directory where the files for the test data sets are located. */
  protected static final String DATA_FILES_DIR = "src" + File.separator + "test" + File.separator + "resources"
      + File.separator + "dbunit" + File.separator;

  protected static final Logger LOG = LoggerFactory.getLogger(BaseDbUnitTest.class);

  /**
   * The DbUnit class used to perform the required unit test steps for setting
   * up and tearing down the test data.
   */
  protected IDatabaseTester databaseTester;
  @Autowired
  protected DataSource dataSource;

  @Override
  protected IDatabaseConnection getConnection() throws Exception {
    java.sql.Connection con = dataSource.getConnection();
    LOG.debug("Connection:"+con.getMetaData().getURL());
    return new DatabaseConnection(con);
  }

  /* What DbUnit does with the existing data and with data in the XML file */
  protected DatabaseOperation getSetUpOperation() throws Exception {
    return DatabaseOperation.CLEAN_INSERT;
  }

  /* What DbUnit does with the data after the test is done. */
  protected DatabaseOperation getTearDownOperation() throws Exception {
    return DatabaseOperation.NONE;
  }

  @Before
  public void setUp() throws Exception {
    LOG.debug("setUp()...");
    super.setUp();
  }
  
  /**
   * Method to load the specified test data file into a data set.
   * 
   * @return The loaded data set.
   * @throws Exception If the data set can not be created successfully.
   */
  @Override
  protected IDataSet getDataSet() throws Exception {
    File testFile = new File(getTestDataPath() + getTestDataFilename() + ".xml");
    LOG.debug("Test file:" + testFile.getAbsolutePath());
    if (!testFile.exists()) {
      LOG.error("Unknown test file:" + testFile.getAbsolutePath());
      throw new Exception("Unknown test file");
    }
    ReplacementDataSet dataSet = initializeReplacementDataSet(testFile, getReplacementDataSet());
    LOG.debug("Current dataSet:"+dataSet.toString());
    return dataSet;
  }

  /**
   * Method to return the path the test file may found in. Overwrite this if you
   * require a different test data file location.
   * 
   * @return The test data file name.
   */
  protected String getTestDataPath() {
    return DATA_FILES_DIR;
  }

  /**
   * Method to return the test file name with contains the XML data to be loaded
   * into the database for the unit test. This is just the file name without a
   * directory or file extension, for example: HomeShoppingOrder or
   * HomeShoppingOrderStatus. The default behaviour will derive the file name
   * from the class name by removing the package name and the Test from the
   * class name (if possible). e.g. za.co.pnp.spring.test.dao.BaseSpringDaoTest
   * will be resolved to BaseSpringDao
   * 
   * @return The test data file name.
   */
  protected String getTestDataFilename() {
    String dataFileName = getClass().getName();
    if (dataFileName.endsWith("Test")) {
      dataFileName = dataFileName.substring(0, dataFileName.length() - 4);
    }
    if (dataFileName.indexOf('.') >= 0) {
      dataFileName = dataFileName.substring(dataFileName.lastIndexOf('.') + 1);
    }
    return dataFileName;
  }

  /**
   * Method to return a Map of values that should be replaced in the DBUnit data
   * file.
   * 
   * To add additional values just override this method in the child class.
   * 
   * @return
   */
  protected static Map<String, Object> getReplacementDataSet() {
    return new HashMap<String, Object>();
  }

  /**
   * Set up the default place holders for replacement data set.
   * 
   * @param replacementDataSet
   *          The current data set.
   * @return The modified data set.
   */
  protected ReplacementDataSet createDefaultReplacementDataSet(ReplacementDataSet replacementDataSet) {
    Calendar now = GregorianCalendar.getInstance();

    Calendar nowMinus3 = GregorianCalendar.getInstance();
    nowMinus3.add(GregorianCalendar.HOUR, -3);

    Calendar nowMinus6 = GregorianCalendar.getInstance();
    nowMinus6.add(GregorianCalendar.HOUR, -6);

    Calendar nowMinus5 = GregorianCalendar.getInstance();
    nowMinus5.add(GregorianCalendar.HOUR, -5);

    Calendar nowMinus05 = GregorianCalendar.getInstance();
    nowMinus05.add(GregorianCalendar.MINUTE, -30);

    replacementDataSet.addReplacementObject("[NOW]", now.getTime());
    replacementDataSet.addReplacementObject("[NOW - 3]", nowMinus3.getTime());
    replacementDataSet.addReplacementObject("[NOW - 6]", nowMinus6.getTime());
    replacementDataSet.addReplacementObject("[NOW - 5]", nowMinus5.getTime());
    replacementDataSet.addReplacementObject("[NOW - 0.5]", nowMinus05.getTime());

    // tomorrow
    Date tomorrow = DateCalcUtil.tomorrow();
    replacementDataSet.addReplacementObject("[tomorrow]", tomorrow);

    // yesterday
    Date yesterday = DateCalcUtil.yesterday();
    replacementDataSet.addReplacementObject("[yesterday]", yesterday);

    // today
    Date today = DateCalcUtil.today();
    replacementDataSet.addReplacementObject("[today]", today);

    // twoDaysAgo
    Date twoDaysAgo = DateCalcUtil.addDays(today, -2);
    replacementDataSet.addReplacementObject("[twoDaysAgo]", twoDaysAgo);
    
    //1weekago
    Date oneWeekAgo = DateCalcUtil.addDays(today, -7);
    replacementDataSet.addReplacementObject("[1weekago]", oneWeekAgo);

    // Null instances/values
    replacementDataSet.addReplacementObject("[NULL]", null);
    replacementDataSet.addReplacementObject("[null]", null);

    // temp directory on local machine
    replacementDataSet.addReplacementObject("[TEMP_DIR]", SystemUtil.TMP_DIR);

    return replacementDataSet;
  }

  protected ReplacementDataSet initializeReplacementDataSet(File dataSet,
      Map<String, Object> additionalReplacementSetValues) throws Exception {
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

