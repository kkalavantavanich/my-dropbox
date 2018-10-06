package my_dropbox.commands;

import my_dropbox.entities.User;
import my_dropbox.services.exception.UserAlreadyExistException;

public class RegisterUserCommand extends AbstractCommand {

	@Override
	public void invoke() {
		User user = new User();
		System.out.print("Enter your username: ");
		String username = console.nextLine();
		if (username.contains(" ")) {
			System.out.println("Username must not contain whitespace(s). User not created.");
			return;
		}
		user.setUsername(username);

		System.out.print("Enter your password: ");
		user.setPassword(console.nextLine());

		System.out.print("Enter your first name: ");
		user.setFirstName(console.nextLine());

		System.out.print("Enter your last name: ");
		user.setLastName(console.nextLine());

		try {
			userService.register(user);
			System.out.println("User registered!");
		} catch (UserAlreadyExistException ex) {
			System.out.println("User Already Existed!");
		}
	}
}
