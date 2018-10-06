package my_dropbox.commands;

import java.util.Scanner;

import my_dropbox.services.FileService;
import my_dropbox.services.UserService;

public abstract class AbstractCommand {

	protected UserService userService;
	protected FileService fileService;
	protected Scanner console;

	public abstract void invoke();

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public FileService getFileService() {
		return fileService;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	public Scanner getConsole() {
		return console;
	}

	public void setConsole(Scanner scanner) {
		this.console = scanner;
	}
}
