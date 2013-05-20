package com.africaapps.league;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * BaseSpringTest is a base unit test class that loads the Spring configuration but does not load any test data using DbUnit.
 * @author aisling
 * @version $Id$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-test.xml" })
public abstract class BaseSpringTest {

}
