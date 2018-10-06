package my_dropbox.commands;

public class LogoutCommand extends AbstractCommand {
	@Override
	public void invoke() {
		if (!userService.isLoggedIn()) {
			System.out.println("User is not logged in!");
		} else {
			userService.logoff();
		}
	}
}
