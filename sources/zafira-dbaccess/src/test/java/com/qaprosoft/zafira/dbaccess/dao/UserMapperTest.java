package com.qaprosoft.zafira.dbaccess.dao;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.qaprosoft.zafira.dbaccess.dao.mysql.UserMapper;
import com.qaprosoft.zafira.dbaccess.model.User;

@Test
@ContextConfiguration("classpath:com/qaprosoft/zafira/dbaccess/dbaccess-test.xml")
public class UserMapperTest extends AbstractTestNGSpringContextTests
{
	/**
	 * Turn this on to enable this test
	 */
	private static final boolean ENABLED = false;

	private static final User USER = new User()
	{
		private static final long serialVersionUID = 1L;
		{
			setUserName("elton");
			setFirstName("Elton");
			setLastName("John");
			setEmail("e.jhon@gmail.com");
			setProject("P1");
		}
	};

	@Autowired
	private UserMapper userMapper;

	@Test(enabled = ENABLED)
	public void createUser()
	{
		userMapper.createUser(USER);

		assertNotEquals(USER.getId(), 0, "User ID must be set up by autogenerated keys");
	}

	@Test(enabled = ENABLED, dependsOnMethods =
	{ "createUser" }, expectedExceptions =
	{ DuplicateKeyException.class })
	public void createUserFail()
	{
		userMapper.createUser(USER);
	}

	@Test(enabled = ENABLED, dependsOnMethods =
	{ "createUser" })
	public void getUserById()
	{
		checkUser(userMapper.getUserById(USER.getId()));
	}

	@Test(enabled = ENABLED, dependsOnMethods =
	{ "createUser" })
	public void getUserByUserName()
	{
		checkUser(userMapper.getUserByUserName(USER.getUserName()));
	}

	@Test(enabled = ENABLED, dependsOnMethods =
	{ "createUser" })
	public void updateUser()
	{
		USER.setUserName("eric");
		USER.setFirstName("Eric");
		USER.setLastName("Clapton");
		USER.setEmail("e.clapton@gmail.com");
		USER.setProject("P2");

		userMapper.updateUser(USER);

		checkUser(userMapper.getUserById(USER.getId()));
	}

	/**
	 * Turn this in to delete user after all tests
	 */
	private static final boolean DELETE_ENABLED = true;

	/**
	 * If true, then <code>deleteUser</code> will be used to delete user after all tests, otherwise -
	 * <code>deleteUserById</code>
	 */
	private static final boolean DELETE_BY_USER = false;

	@Test(enabled = ENABLED && DELETE_ENABLED && DELETE_BY_USER, dependsOnMethods =
	{ "createUser", "createUserFail", "getUserById", "getUserByUserName", "updateUser" })
	public void deleteUser()
	{
		userMapper.deleteUser(USER);

		assertNull(userMapper.getUserById(USER.getId()));
	}

	@Test(enabled = ENABLED && DELETE_ENABLED && !DELETE_BY_USER, dependsOnMethods =
	{ "createUser", "createUserFail", "getUserById", "getUserByUserName", "updateUser" })
	public void deleteUserById()
	{
		userMapper.deleteUserById((USER.getId()));

		assertNull(userMapper.getUserById(USER.getId()));
	}

	private void checkUser(User user)
	{
		assertEquals(user.getUserName(), USER.getUserName(), "User name must match");
		assertEquals(user.getFirstName(), USER.getFirstName(), "First name must match");
		assertEquals(user.getLastName(), USER.getLastName(), "Last name must match");
		assertEquals(user.getEmail(), USER.getEmail(), "Email must match");
		assertEquals(user.getProject(), USER.getProject(), "Project must match");
	}
}
