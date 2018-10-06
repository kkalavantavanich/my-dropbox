package my_dropbox.commands;

import my_dropbox.services.exception.ResourceNotFoundException;

public class GetCommand extends AbstractCommand {
	@Override
	public void invoke() {
		System.out.print("Enter resource filename: ");
		String filename = console.nextLine();
		try {
			fileService.download(filename);
			System.out.format("Downloaded %s from storage\n", filename);
		} catch (ResourceNotFoundException ex) {
			System.out.format("Cannot find file %s in storage\n", filename);
		}
	}
}
