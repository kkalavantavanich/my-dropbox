package my_dropbox.commands;

import my_dropbox.services.exception.UserLoginInvalidException;

public class LoginCommand extends AbstractCommand {

	@Override
	public void invoke() {
		if (userService.isLoggedIn()) {
			System.out.println("User already logged in!");
			return;
		}
		System.out.print("Enter your username: ");
		String username = console.nextLine();

		System.out.print("Enter your password: ");
		String password = console.nextLine();

		try {
			userService.login(username, password);
			System.out.format("Hello, %s!\n", userService.getCurrentUser().getDisplayName());
		} catch (UserLoginInvalidException ex) {
			System.out.println("Username or password is not correct.");
		}
	}
}
