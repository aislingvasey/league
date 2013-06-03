package com.africaapps.league.dao.game.hibernate;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.africaapps.league.BaseSpringDbUnitTest;
import com.africaapps.league.dao.game.UserDao;
import com.africaapps.league.model.game.User;
import com.africaapps.league.service.transaction.WriteTransaction;

public class UserDaoTest extends BaseSpringDbUnitTest {

	@Autowired
	private UserDao userDao;
	
	@WriteTransaction
	@Test
	public void saveAndGetUsers() throws Exception {
		String username = "bob";
		String pwd = "secret";
		String username2 = "mxitBob";
		
		assertFalse(userDao.isExistingUsername(username));
		assertFalse(userDao.isExistingUsername(username2));
		User user = userDao.getUser(username, pwd);
		assertNull(user);
		user = userDao.getUser(username2, null);
		assertNull(user);
		
		//Save
		user = new User();
		user.setUsername(username);
		user.setPassword(pwd);
		user.setCellNumber("082 567 8901");
		user.setEmailAddress("bob@somewhere.com");
		user.setFirstName("Bob");
		user.setLastName("Surname");
		userDao.saveOrUpdate(user);
		//Check saved
		assertTrue(userDao.isExistingUsername(username));
		User bob = userDao.getUser(username, pwd);
		assertNotNull(bob);
		assertNotNull(bob.getId());
		assertEquals(user.getCellNumber(), bob.getCellNumber());
		assertEquals(user.getEmailAddress(), bob.getEmailAddress());
		assertEquals(user.getFirstName(), bob.getFirstName());
		assertEquals(user.getLastName(), bob.getLastName());
		assertEquals(user.getPassword(), bob.getPassword());
		assertEquals(user.getUsername(), bob.getUsername());
		
		//Save another
		User mxitBob = new User();
		mxitBob.setCellNumber(null);
		mxitBob.setEmailAddress(null);
		mxitBob.setFirstName("Bob");
		mxitBob.setLastName("Surname");
		mxitBob.setUsername(username2);
		userDao.saveOrUpdate(mxitBob);
		assertTrue(userDao.isExistingUsername(username2));
		bob = userDao.getUser(username2, null);
		assertNotNull(bob);
		assertNotNull(bob.getId());
		assertEquals(mxitBob.getCellNumber(), bob.getCellNumber());
		assertEquals(mxitBob.getEmailAddress(), bob.getEmailAddress());
		assertEquals(mxitBob.getFirstName(), bob.getFirstName());
		assertEquals(mxitBob.getLastName(), bob.getLastName());
		assertEquals(mxitBob.getPassword(), bob.getPassword());
		assertEquals(mxitBob.getUsername(), bob.getUsername());
		
		//User with duplicate username
		User bob2 = new User();
		bob2.setUsername(username);
		try {
			userDao.saveOrUpdate(bob2);
			assertTrue(userDao.isExistingUsername(username)); //force the previous stmt to be flushed before the exception is thrown
			fail();
		} catch (ConstraintViolationException e) {
			//expected
		}
	}
}
