package my_dropbox.commands;

import java.net.URL;
import java.util.Objects;

import my_dropbox.services.exception.ResourceNotFoundException;

public class PutCommand extends AbstractCommand {

	@Override
	public void invoke() {
		System.out.print("Enter resource filename: ");
		String filename = console.nextLine();
		try {
			fileService.upload(getResource(filename));
			System.out.format("File %s uploaded.%n", filename);
		} catch (ResourceNotFoundException ex) {
			System.out.format("File '%s' not found in resources folder.%n", filename);
		}
	}

	private static String getResource(String resourceName) {
		ClassLoader classLoader = PutCommand.class.getClassLoader();
		try {
			URL url = Objects.requireNonNull(classLoader.getResource(resourceName));
			return url.getFile();
		} catch (NullPointerException ex) {
			throw new ResourceNotFoundException();
		}
	}
}
