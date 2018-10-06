package my_dropbox.repositories;

import my_dropbox.entities.User;
import org.junit.Test;

public class UserRepositoryTest {

	UserRepository underTest = new UserRepositoryImpl();

	@Test
	public void test() {
		User user = new User();
		user.setUsername("abc");
		user.setPassword("def");
		user.setFirstName("ghi");
		user.setLastName("jkl");
		underTest.create(user);

		underTest.find("abc");

		underTest.delete("abc");
	}
}
