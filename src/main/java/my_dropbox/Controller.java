package my_dropbox;

import java.util.Scanner;

import my_dropbox.commands.GetCommand;
import my_dropbox.commands.LoginCommand;
import my_dropbox.commands.LogoutCommand;
import my_dropbox.commands.PutCommand;
import my_dropbox.commands.RegisterUserCommand;
import my_dropbox.commands.ViewCommand;
import my_dropbox.services.FileService;
import my_dropbox.services.FileServiceImpl;
import my_dropbox.services.UserService;
import my_dropbox.services.UserServiceImpl;

public class Controller {

	private UserService userService;
	private FileService fileService;

	private RegisterUserCommand registerUserCommand;
	private LoginCommand loginCommand;
	private LogoutCommand logoutCommand;
	private PutCommand putCommand;
	private ViewCommand viewCommand;
	private GetCommand getCommand;

	private boolean running;
	private Scanner console;

	public Controller() {
		userService = new UserServiceImpl();
		fileService = new FileServiceImpl();
		fileService.setUserService(userService);

		console = new Scanner(System.in);

		initializeCommands();
	}

	public void run() {
		System.out.println("Welcome to My Dropbox!");
		System.out.println("Type 'help' for a list of all commands");
		running = true;

		while(running) {
			String prompt = getPrompt();
			System.out.print(prompt);
			String command = console.nextLine();

			switch (command) {
			case "newuser":
				registerUserCommand.invoke();
				break;
			case "login":
				loginCommand.invoke();
				break;
			case "logout":
				logoutCommand.invoke();
				break;
			case "put":
				putCommand.invoke();
				break;
			case "get":
				getCommand.invoke();
				break;
			case "view":
				viewCommand.invoke();
				break;
			case "quit":
				running = false;
				break;
			case "help":
				showHelp();
				break;
			default:
				System.out.println("Unknown command '" + command + "'");
			}
		}
	}

	private String getPrompt() {
		String prompt = "";
		if (userService.isLoggedIn()) {
			prompt = userService.getCurrentUser().getDisplayName();
		}
		prompt += "$> ";
		return prompt;
	}

	private void initializeCommands() {
		registerUserCommand = new RegisterUserCommand();
		registerUserCommand.setConsole(console);
		registerUserCommand.setUserService(userService);
		registerUserCommand.setFileService(fileService);

		loginCommand = new LoginCommand();
		loginCommand.setConsole(console);
		loginCommand.setUserService(userService);
		loginCommand.setFileService(fileService);

		logoutCommand = new LogoutCommand();
		logoutCommand.setConsole(console);
		logoutCommand.setUserService(userService);
		logoutCommand.setFileService(fileService);

		putCommand = new PutCommand();
		putCommand.setConsole(console);
		putCommand.setUserService(userService);
		putCommand.setFileService(fileService);

		viewCommand = new ViewCommand();
		viewCommand.setConsole(console);
		viewCommand.setUserService(userService);
		viewCommand.setFileService(fileService);

		getCommand = new GetCommand();
		getCommand.setConsole(console);
		getCommand.setUserService(userService);
		getCommand.setFileService(fileService);
	}

	private void showHelp() {
		showHelpForCommand("newuser", "Creates a new user");
		showHelpForCommand("login", "Login into the system");
		showHelpForCommand("logout", "Logout of the system");
		showHelpForCommand("put", "Uploads a new file to the storage");
		showHelpForCommand("get", "Downloads an existing file from the storage");
		showHelpForCommand("view", "List all of the files in the storage");
		showHelpForCommand("quit", "Exits the system");
		showHelpForCommand("help", "Show help message");
	}

	private void showHelpForCommand(String command, String description) {
		String leftAlignFormat = "%-10s - %-50s%n";
		System.out.format(leftAlignFormat, command, description);
	}
}
