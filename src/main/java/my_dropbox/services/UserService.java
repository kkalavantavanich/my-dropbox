package my_dropbox.services;

import my_dropbox.entities.User;

public interface UserService {
	void register(User user);
	User getCurrentUser();
	void login(String username, String password);
	void logoff();
	boolean isLoggedIn();
}
