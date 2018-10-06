package my_dropbox.commands;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.amazonaws.services.s3.model.S3ObjectSummary;

public class ViewCommand extends AbstractCommand {

	@Override
	public void invoke() {
		List<S3ObjectSummary> summaries = fileService.getFileSummaries();
		if (summaries.isEmpty()) {
			System.out.format("No files found for %s\nAdd a new file using the 'put' command.%n", userService.getCurrentUser().getDisplayName());
		} else {
			printFileSummary(summaries);
		}
	}

	private void printFileSummary(List<S3ObjectSummary> summaries) {
		String leftAlignFormat = "| %-20s | %-9s | %-19s |%n";
		printTableHorizontal();
		System.out.format(leftAlignFormat, "File Name", "File Size", "Last Modified Date");
		printTableHorizontal();
		for(S3ObjectSummary summary: summaries) {
			String fileName = formatFileName(summary.getKey().replace(userService.getCurrentUser().getUsername() + "/", ""));
			String fileSize = formatFileSize(summary.getSize());
			String lastModified = formatDate(summary.getLastModified());
			System.out.format(leftAlignFormat, fileName, fileSize, lastModified);
		}
		printTableHorizontal();
	}

	private void printTableHorizontal() {
		System.out.format("+----------------------+-----------+---------------------+%n");
	}

	private static String formatFileName(String fileName) {
		if (fileName.length() > 20) {
			return fileName.substring(0, 17) + "...";
		} else {
			return fileName;
		}
	}

	private static String formatFileSize(long fileSize) {
		long KILOBYTE = (1L << 10);
		long MEGABYTE = (1L << 20);
		long GIGABYTE = (1L << 30);

		String formattedFileSize;
		if (fileSize >= GIGABYTE) {
			formattedFileSize = String.format("%.1fG", (((float) fileSize) / ((float) GIGABYTE)));
		} else if (fileSize >= MEGABYTE) {
			formattedFileSize = String.format("%.1fM", (((float) fileSize) / ((float) MEGABYTE)));
		} else if (fileSize >= KILOBYTE) {
			formattedFileSize = String.format("%.1fM", (((float) fileSize) / ((float) KILOBYTE)));
		} else {
			formattedFileSize = String.format("%dB", fileSize);
		}

		return formattedFileSize;
	}

	private static String formatDate(Date date) {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		return simpleDateFormat.format(date);
	}
}
